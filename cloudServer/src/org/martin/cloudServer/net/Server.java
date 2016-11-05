/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and openConnection the template in the editor.
 */
package org.martin.cloudServer.net;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.martin.cloudCommon.system.Command;
import org.martin.cloudCommon.system.SysInfo;
import org.martin.cloudServer.db.DbManager;
import org.martin.cloudServer.net.threads.TClient;
import org.martin.cloudServer.net.threads.TOperatorRequest;
import org.martin.cloudServer.system.AccountManager;
import org.martin.electroList.structure.ElectroList;

/**
 *
 * @author martin
 */
public class Server extends Thread{
    private final ServerSocket serverSock;
    private final ElectroList<TClient> tClientsRunning;
    //private int tClientsCount;
    //private final DbManager dbManager;
    private final File rootDirectory;
    public static Server server;

    public static void newInstance() throws IOException, SQLException{
        server = new Server();
    }
    
    public static void newInstance(int port) throws IOException, SQLException{
        server = new Server(port);
    }
    
    public static Server getInstance(){
        return server;
    }
    
    private Server() throws IOException, SQLException {
        this(SysInfo.DEFAULT_PORT);
    }
    
    private Server(int port) throws IOException, SQLException{
        serverSock = new ServerSocket(port);
        tClientsRunning = new ElectroList<>();
        rootDirectory = new File(SysInfo.ROOT_FOLDER_NAME);
        if(!rootDirectory.exists()) rootDirectory.mkdir();
        //dbManager = new DbManager();
    }
    
//    public synchronized boolean isNickAvailable(User user){
//        return dbManager.isNickAvailable(user);
//    }

//    public synchronized boolean isNickAvailable(String nick) throws SQLException{
//        return dbManager.isNickAvailable(nick);
//    }
//    
//    public synchronized boolean isValidUser(User user) throws SQLException{
//        return dbManager.isValidUser(user.getNick(), user.getPassword());
//    }
//    
//    public synchronized boolean isValidUser(String user, String passw) throws SQLException{
//        return dbManager.isValidUser(user, passw);
//    }

    private void runOperadorRequest(Socket sockRequest) throws IOException{
        new Thread(new TOperatorRequest(sockRequest)).start();
    }
    
    public synchronized boolean isClientConnected(long userId){
        return tClientsRunning.stream()
                .anyMatch(tc -> tc.getClient().getIdUser() == userId);
    }
    
    public synchronized boolean isClientConnected(String nick){
        return tClientsRunning.stream()
                .anyMatch(tc -> tc.getClient().getUser().getNick().equals(nick));
    }
    
    public synchronized void addClient(Client client) throws SocketException, SQLException{
        client.getSocket().setSoLinger(true, 3);
        tClientsRunning.add(new TClient(client));
    }
    
    public synchronized boolean addUser(String nick, String passw) throws SQLException, IOException{
        DbManager dbMan = new DbManager();
        final boolean isValid = dbMan.isNickAvailable(nick);
        
        if (isValid)
            dbMan.addUser(nick, passw);
        
        AccountManager.createRootDirectory(dbMan.getUserByNick(nick));
        dbMan.closeConnection();
        dbMan = null;

        return isValid;
    }
    
//    public synchronized void addUser(User user) throws IOException, SQLException{
//        dbManager.addUser(user);
//        AccountManager.createRootDirectory(user);
//    }
//
//    public synchronized boolean addUser(String nick, String passw) throws SQLException, IOException{
//        final boolean isValid = isNickAvailable(nick);
//        if (isValid) 
//            addUser(new User(nick, passw));
//        
//        return isValid;
//    }
//
//    public synchronized void removeUser(int id) throws SQLException{
//        dbManager.removeUser(id);
//    }
//    
//    public synchronized void removeUser(String nick) throws SQLException{
//        dbManager.removeUserByNick(nick);
//    }
//    
//    public synchronized long getUsersCount() throws SQLException{
//        return dbManager.getUsersCount();
//    }
//    
//    public synchronized User getUser(String nick) throws SQLException{
//        return dbManager.getUserByNick(nick);
//    }
//    
//    public synchronized User getUser(String nick, String password) throws SQLException{
//        boolean existsUser = isValidUser(nick, password);
//        if (existsUser) return getUser(nick);
//        return new User();
//        
//    }
    
    public synchronized void removeClient(long id){
        tClientsRunning.removeIf(tc -> tc.getClient().getIdUser() == id);
    }
    
    public synchronized void removeClient(String nick){
        tClientsRunning
                .removeIf(tc -> tc.getClient().getUser().getNick().equals(nick));
    }
    
    public int getClientsCount(){
        return tClientsRunning.size();
    }
    
    @Override
    public void run(){
        while (true) {
            try {
                System.out.println("Antes del nuevo cliente");
                runOperadorRequest(serverSock.accept());
                System.out.println("Un cliente llegó!");
                Thread.sleep(300);
            } catch (InterruptedException | IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public static void main(String[] args) throws URISyntaxException, CloneNotSupportedException {
        try {
            Scanner s = new Scanner(System.in);
            Command cmd = null;
            String strCommand;
            
            do {                
                System.out.print("> ");
                strCommand = s.nextLine();
                if(!strCommand.isEmpty()) cmd = new Command(strCommand);
                
            } while (cmd == null || !cmd.isValid());

            if(cmd.getOptionsCount() == 4) 
                Thread.sleep(Integer.parseInt(cmd.getOptionAt(3))*1000);
                
            if (cmd.getOptionAt(1).equals("default"))
                Server.newInstance();
            else
                Server.newInstance(Integer.parseInt(cmd.getOptionAt(1)));

            Server.getInstance().start();
            
            strCommand = null;
            cmd = null;
            s = null;
        } catch (IOException | SQLException | InterruptedException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
