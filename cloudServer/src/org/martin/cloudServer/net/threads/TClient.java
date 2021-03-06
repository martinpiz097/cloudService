/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudServer.net.threads;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.martin.cloudCommon.interfaces.Receivable;
import org.martin.cloudCommon.interfaces.Transmissible;
import org.martin.cloudCommon.model.packages.TransferPackage;
import org.martin.cloudServer.system.AccountManager;
import org.martin.cloudCommon.system.Command;
import org.martin.cloudServer.db.DbManager;
import org.martin.cloudServer.net.Client;
import org.martin.cloudServer.system.CommandInterpreter;

/**
 *
 * @author martin
 */
public class TClient extends Thread implements Transmissible, Receivable{
    private Client client;
    private CommandInterpreter ci;
    private DbManager dbManager;
    
    private boolean isConnected;
    //private static final StringBuilder sBuilder = new StringBuilder();
    
    public TClient(Client client) throws SQLException {
        this.client = client;
        ci = new CommandInterpreter();
        isConnected = true;
        dbManager = new DbManager(false);
        setThreadName();
        start();
    }
    
    private void cancelAll(){
        client = null;
        ci = null;
        dbManager = null;
    }

    private void setThreadName(){
        StringBuilder sBuilder = new StringBuilder();
        sBuilder.append(client.getUser().getNick());
        sBuilder.append("Thread");
        setName(sBuilder.toString());
        sBuilder.delete(0, sBuilder.length());
        sBuilder = null;
    }
    
    public void closeStreams() throws IOException{
        client.closeStreams();
    }
    
    public void closeConnection() throws IOException, SQLException{
        client.closeConnection();
        dbManager.closeConnection();
        cancelAll();
        isConnected = false;
    }
    
    public boolean hasDataReceived() throws IOException, ClassNotFoundException{
        return getReceivedObject() != null;
    }
    
    public Client getClient(){
        return client;
    }

    public AccountManager getAccountManager(){
        return client.getAccountManager();
    }

    public DbManager getDbManager() {
        return dbManager;
    }

    public void execTransfer(TransferPackage tp){
        new Thread(() -> {
            try {
                new CommandInterpreter().execParallelCommand(tp, this);
            } catch (IOException | SQLException ex) {
                Logger.getLogger(TClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
    
    @Override
    public void sendObject(Object obj) throws IOException {
        client.getOutput().writeObject(obj);
    }

    @Override
    public Object getReceivedObject() throws IOException, ClassNotFoundException {
        return client.getInput().readObject();
    }
    
    @Override
    public void run(){
        Object objReceived;
        TransferPackage transPack;
        Command cmd;

        while (isConnected) {
            try {
                System.out.println("Antes del nuevo objeto en TCLIENT");
                objReceived = null;
                while (objReceived == null)
                    objReceived = getReceivedObject();
                System.out.println("Ha llegado un objeto en TCLIENT");
                System.out.println("Clase del objeto: " + objReceived.getClass().getName());
                
                if (objReceived instanceof TransferPackage) {
                    transPack = (TransferPackage) objReceived;
                    System.out.println("Entramos al if del transPack; cmd: " + transPack.getCommand());
                    if (transPack.getCommand().isValid()) {
                        System.out.println("El comando es valido");
                        ci.execSpecialCommand(transPack, this);
                        //execTransfer(transPack);
                    }
                    transPack = null;
                }
                else if (objReceived instanceof Command) {
                    cmd = (Command) objReceived;
                    if (cmd.isValid()) ci.execCommand(cmd, this);
                    cmd = null;
                }
                
                Thread.sleep(300);
            } catch (InterruptedException | IOException | ClassNotFoundException | SQLException ex) {
                Logger.getLogger(TClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Hilo cliente "+client.getUser().getNick()+" finalizado");
    }
}

