/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and openConnection the template in the editor.
 */
package org.martin.cloudServer.db;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import org.martin.cloudCommon.model.User;
import org.martin.cloudCommon.system.Account;
import org.martin.cloudCommon.system.SysInfo;
/**
 *
 * @author martin
 */
public final class DbManager {
    
    private DbConnection connection;

    public enum TYPE_USER{
        ENABLED, DISABLED, ALL;
    }
    
    public DbManager() throws SQLException {
        openConnection();
    }
    
    public DbManager(boolean open) throws SQLException{
        if (open) openConnection();
        else connection = null;
    }

    public boolean isOpenConnection(){
        return connection != null;
    }
    
    public void openConnection() throws SQLException{
        connection = new DbConnection("localhost", "dbCloud", "root", "admin");
    }
    
    public void closeConnection() throws SQLException{
        connection.close();
        connection = null;
    }
    
//    public boolean isNickAvailable(User newUser) throws SQLException{
//        final int cantResults = connection.getTableCount("user", "nick='"+newUser.getNick()+"' "
//                + "and code = '"+newUser.getPassword()+"'");
//        
//        return cantResults == 0;
//    }

    public void cancelVariable(Object... objs){
        for (Object obj : objs)
            if (obj instanceof Number)
                obj = 0;
            else if (!(obj instanceof Boolean))
                obj = null;
            
    }
    
    public boolean isNickAvailable(String nick) throws SQLException {
        final boolean isValid;
        try (ResultSet res = connection.select("isNickAvailable", nick)) {
            if (res.next()) isValid = res.getBoolean(1);
            else isValid = true;

            res.close();
            cancelVariable(res);
        }

        return isValid;
    }
    
    public boolean isValidUser(String nick, String passw) throws SQLException{
        final boolean isValid;
        try (ResultSet res = connection.select("isValidUser", nick, passw)) {
            if (res.next()) isValid = res.getBoolean(1);
            else isValid = true;
            
            res.close();
            cancelVariable(res);
        }
        return isValid;
    }

    public long getUsersCount() throws SQLException{
        final long count;
        try (ResultSet res = connection.select("getUsersCount")) {
            if(res.next()) count = res.getLong(1);
            else count = 0;
            
            res.close();
            cancelVariable(res);
        }
        return count;
    }
    
    public long getAccountsCount() throws SQLException{
        final long count;
        try (ResultSet res = connection.select("getAccountsCount")) {
            if(res.next()) count = res.getLong(1);
            else count = 0;
        
            res.close();
            cancelVariable(res);
        }
        return count;
    }
    
    public long getUsedSpace(long idAccount) throws SQLException{
        long usedSpace;
        try (ResultSet res = connection.select("getUsedSpace", idAccount)) {
            if(res.next()) usedSpace = res.getLong(1);
            else usedSpace = 0;
        
            res.close();
            cancelVariable(res);
        }
        return usedSpace;
    }
    
    public long getTotalSpace(long idAccount) throws SQLException{
        long totalSpace;
        try (ResultSet res = connection.select("getTotalSpace", idAccount)) {
            if(res.next()) totalSpace = res.getLong(1);
            else totalSpace = 0;
        
            res.close();
            cancelVariable(res);
        }
        return totalSpace;
    }
    
    public long getFreeSpace(long idAccount) throws SQLException{
        long freeSpace;
        try (ResultSet res = connection.select("getFreeSpace", idAccount)) {
            if(res.next()) freeSpace = res.getLong(1);
            else freeSpace = 0;
        
            res.close();
            cancelVariable(res);
        }
        return freeSpace;
    }
    
    public boolean hasAvailableSpace(long fileSize, long idAccount) throws SQLException{
        final boolean hasAvailableSpace;
        try (ResultSet res = connection.select("hasAvailableSpace", fileSize, idAccount)) {
            if(res.next()) hasAvailableSpace = res.getBoolean(1);
            else hasAvailableSpace = false;
        
            res.close();
            cancelVariable(res);
        }
        return hasAvailableSpace;
    }
    
    public boolean hasAvailableSpace(File newFile, long idAccount) throws SQLException{
        return hasAvailableSpace(newFile.length(), idAccount);
    }
    
    public long getAccountsCountByUser(long idUser) throws SQLException{
        final long count;
        try(ResultSet res = connection.select("getAccountsCountByUser", idUser)){
            if(res.next()) count = res.getLong(1);
            else count = 0;
            
            res.close();
            cancelVariable(res);
        }
        return count;
    }
    
    public void addUser(User user) throws SQLException{
        addUser(user.getNick(), user.getPassword());
    }

    public void addUser(String nick, String passw) throws SQLException{
        connection.call("addUser", nick, passw);
        addAccount(new Account(getUserByNick(nick), SysInfo.TOTAL_SPACE));
    }
    
    public LinkedList<User> getUsers(TYPE_USER typeUser) throws SQLException{
        final LinkedList<User> users = new LinkedList<>();
        ResultSet res;
        String procedureName;
        
        switch(typeUser){
            case ENABLED:
                procedureName = "getEnabledUsers";
                break;
            case DISABLED:
                procedureName = "getDisabledUsers";
                break;
            case ALL:
                procedureName = "getUsers()";
                break;
            default:
                procedureName = null;
                break;
        }
        res = connection.call(procedureName);
        
        while (res.next())
            users.add(new User(res.getLong(1), res.getString(2), res.getString(3)));
        
        res.close();
        cancelVariable(res, procedureName);
        return users;
    }
    
    public User getUser(long id) throws SQLException{
        final User user;
        try (ResultSet res = connection.call("getUser", id)) {
            if(res.next())
                user = new User(res.getLong(1), res.getString(2), res.getString(3));
            else
                user = null;
            
            res.close();
            cancelVariable(res);
        }
        return user;
    }
    
    public User getLastUser() throws SQLException{
        final User user;
        try (ResultSet res = connection.call("getLastUser")) {
            if(res.next())
                user = new User(res.getLong(1), res.getString(2), res.getString(3));
            else
                user = null;
        
            res.close();
            cancelVariable(res);
        }
        return user;
    }
    
    public User getUserByNick(String nick) throws SQLException{
        final User user;
        try (ResultSet res = connection.call("getUserByNick", nick)) {
            if(res.next())
                user = new User(res.getLong(1), res.getString(2), res.getString(3));
            else
                user = null;
        
            res.close();
            cancelVariable(res);
        }
        return user;
    }

    public User getUserByNickAndPassw(String nick, String passw) throws SQLException{
        final User user;
        try (ResultSet res = connection.call("getUserByNickAndPassw", nick, passw)) {
            if(res.next())
                user = new User(res.getLong(1), res.getString(2), res.getString(3));
            else
                user = new User();
        
            res.close();
            cancelVariable(res);
        }
        return user;
    }
    
    public void addAccount(Account account) throws SQLException{
        connection.call("addAccount", account.getUser().getId(), account.getRootDirName(), 
                account.getUsedSpace(), account.getTotalSpace());
    }

    public void addFile(long fileSize, long idAccount) throws SQLException{
        connection.call("addFile", fileSize, idAccount);
    }
    
    public void removeFile(long fileSize, long idAccount) throws SQLException{
        connection.call("removeFile", fileSize, idAccount);
    }
    
    public Account getAccount(long id) throws SQLException{
        final Account account;
        try (ResultSet res = connection.call("getAccount", id)) {
            if (res.next()) account = new Account(res.getLong(1), getUser(res.getLong(2)), res.getString(3),
                    res.getLong(4), res.getLong(5), res.getTimestamp(6));
            
            else account = null;
        
            res.close();
            cancelVariable(res);
        }
        return account;
    }

    public Account getAccountByUser(long idUser) throws SQLException{
        final Account account;
        try (ResultSet res = connection.call("getAccountsByUser", idUser)) {
            if (res.next()) account = new Account(res.getLong(1), getUser(res.getLong(2)), res.getString(3),
                    res.getLong(4), res.getLong(5), res.getTimestamp(6));
            
            else account = null;
            
            res.close();
            cancelVariable(res);
        }
        return account;
    }

    public void removeAccount(long id) throws SQLException{
        connection.call("removeAccount", id);
        
    }
    
    public void removeAccountByUser(long idUser) throws SQLException{
        connection.call("removeAccountByUser", idUser);
    }
    
    public void removeAllAccounts() throws SQLException{
        connection.call("removeAllAccounts");
    }
    
    public void removeUser(long id) throws SQLException{
        connection.call("removeUser", id);
    }
    
    public void removeUserByNick(String nick) throws SQLException{
        connection.call("removeUserByNick", nick);
    }
    
    public void removeAllUsers() throws SQLException{
        connection.call("removeAllUsers");
    }
    
//    private void printUsers(){
//        System.out.println("Usuarios\n--------\n");
//        System.out.println("Cantidad de usuarios: " + users.size());
//        users.forEach(System.out::println);
//    }
    
}