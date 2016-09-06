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
import org.martin.cloudServer.net.Client;
import org.martin.cloudServer.system.CommandInterpreter;

/**
 *
 * @author martin
 */
public class TClient extends Thread implements Transmissible, Receivable{
    private final Client client;
    private final CommandInterpreter ci;
    private boolean isConnected;
    
    public TClient(Client client) {
        this.client = client;
        ci = new CommandInterpreter();
        isConnected = true;
        start();
    }

    public void closeStreams() throws IOException{
        client.closeStreams();
    }
    
    public void closeConnection() throws IOException{
        client.closeConnection();
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
                // Queda pendiente las operaciones con el cloud
                // ya que deben hacerse por comandos

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
                    }
                }
                else if (objReceived instanceof Command) {
                    cmd = (Command) objReceived;
                    if (cmd.isValid()) ci.execCommand(cmd, this);
                }
                
                Thread.sleep(300);
            } catch (InterruptedException | IOException | ClassNotFoundException | SQLException ex) {
                Logger.getLogger(TClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Hilo cliente "+client.getUser().getNick()+" finalizado");
    }
}
