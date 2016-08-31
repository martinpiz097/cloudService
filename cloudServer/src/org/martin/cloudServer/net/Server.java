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

    public synchronized boolean isValidRegistration(String nick) throws SQLException{
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
    
    public synchronized void addUser(User user) throws IOException{
        dbManager.addUser(user);
    }

    public synchronized boolean addUser(String nick, String passw) throws SQLException{
        final boolean isValid = isValidRegistration(nick);
        if (isValid) dbManager.addUser(nick, passw);
        return isValid;
    }

    public synchronized void removeUser(int id){
        dbManager.removeUser(id);
    }
    
    public synchronized void removeUser(String nick){
        dbManager.removeUser(nick);
    }
    
    public synchronized int getUsersCount(){
        return dbManager.getUsersCount();
    }
    
    public synchronized User getUser(String nick){
        return dbManager.getUser(nick);
    }
    
    public synchronized User getUser(String nick, String password){
        User u = dbManager.getUser(nick, password);
        return u == null ? new User() : u;
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
