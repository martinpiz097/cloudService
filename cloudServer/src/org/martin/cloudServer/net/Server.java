/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudServer.net;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.martin.cloudCommon.model.DefaultUser;
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

    public static void newInstace() throws IOException{
        server = new Server();
    }
    
    public static Server getInstance(){
        return server;
    }
    
    public Server() throws IOException {
        serverSock = new ServerSocket(SysInfo.DEFAULT_PORT);
        tClientsRunning = new LinkedList<>();
        tClientsCount = 0;
        rootDirectory = new File(SysInfo.ROOT_FOLDER_NAME);
        if(!rootDirectory.exists()) rootDirectory.mkdir();
        dbManager = new DbManager();
    }
    
    public synchronized boolean isValidRegistration(DefaultUser user){
        return dbManager.isValidRegistration(user);
    }

    public synchronized boolean isValidRegistration(String nick, String passw){
        return dbManager.isValidRegistration(nick, passw);
    }
    
    public synchronized boolean isValidUser(DefaultUser user){
        return dbManager.isValidUser(user.getNick(), user.getPassword());
    }
    
    public synchronized boolean isValidUser(String user, String passw){
        return dbManager.isValidUser(user, passw);
    }
    
    public synchronized void addClient(Client client){
        tClientsRunning.add(new TClient(client));
        tClientsCount++;
    }
    
    public synchronized void addUser(DefaultUser user) throws IOException{
        dbManager.addUser(user);
    }

    public synchronized boolean addUser(String nick, String passw){
        final boolean isValid = isValidRegistration(nick, passw);
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
                new TOperatorRequest(serverSock.accept()).start();
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
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
