/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudServer.net;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.martin.cloudCommon.model.User;
import org.martin.cloudCommon.model.User;
import org.martin.cloudCommon.system.SysInfo;
import org.martin.cloudServer.db.DbManager;
import org.martin.cloudServer.net.threads.TClient;
import org.martin.cloudServer.net.threads.TOperatorRequest;

/**
 *
 * @author martin
 */
public class Server extends Thread{
    
    private final ServerSocket serverSock;
    private final LinkedList<TClient> tClientsRunning;
    private int tClientsCount;
    private final DbManager dbManager;
    private final File rootDirectory;
    public static Server server;

    public static void newInstace() throws IOException, SQLException{
        server = new Server();
    }
    
    public static Server getInstance(){
        return server;
    }
    
    public Server() throws IOException, SQLException {
        serverSock = new ServerSocket(SysInfo.DEFAULT_PORT);
        tClientsRunning = new LinkedList<>();
        tClientsCount = 0;
        rootDirectory = new File(SysInfo.ROOT_FOLDER_NAME);
        if(!rootDirectory.exists()) rootDirectory.mkdir();
        dbManager = new DbManager();
    }
    
//    public synchronized boolean isNickAvailable(User user){
//        return dbManager.isNickAvailable(user);
//    }

    public synchronized boolean isNickAvailable(String nick) throws SQLException{
        return dbManager.isNickAvailable(nick);
    }
    
    public synchronized boolean isValidUser(User user) throws SQLException{
        return dbManager.isValidUser(user.getNick(), user.getPassword());
    }
    
    public synchronized boolean isValidUser(String user, String passw) throws SQLException{
        return dbManager.isValidUser(user, passw);
    }
    
    public synchronized void addClient(Client client){
        tClientsRunning.add(new TClient(client));
        tClientsCount++;
    }
    
    public synchronized void addUser(User user) throws IOException, SQLException{
        dbManager.addUser(user);
    }

    public synchronized boolean addUser(String nick, String passw) throws SQLException{
        final boolean isValid = isNickAvailable(nick);
        if (isValid) dbManager.addUser(nick, passw);
        return isValid;
    }

    public synchronized void removeUser(int id) throws SQLException{
        dbManager.removeUser(id);
    }
    
    public synchronized void removeUser(String nick) throws SQLException{
        dbManager.removeUserByNick(nick);
    }
    
    public synchronized long getUsersCount() throws SQLException{
        return dbManager.getUsersCount();
    }
    
    public synchronized User getUser(String nick) throws SQLException{
        return dbManager.getUserByNick(nick);
    }
    
    public synchronized User getUser(String nick, String password) throws SQLException{
        boolean existsUser = isValidUser(nick, password);
        if (existsUser) return getUser(nick);
        return new User();
        
    }
    
    public synchronized void removeClient(int id){
        tClientsRunning.removeIf(tc -> tc.getClient().getIdUser() == id);
        tClientsCount--;
    }
    
    public synchronized void removeClient(String nick){
        tClientsRunning
                .removeIf(tc -> tc.getClient().getUser().getNick().equalsIgnoreCase(nick));
        tClientsCount--;
    }
    
    public int getClientsCount(){
        return tClientsCount;
    }
    
    @Override
    public void run(){
        while (true) {
            try {
                System.out.println("Antes del nuevo cliente");
                new TOperatorRequest(serverSock.accept()).start();
                System.out.println("Un cliente llegó!");
                Thread.sleep(300);
            } catch (InterruptedException | IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void main(String[] args) {
        try {
            Server.newInstace();
            Server.getInstance().start();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
