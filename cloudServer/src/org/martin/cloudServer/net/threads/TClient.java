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
import org.martin.cloudCommon.model.User;
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
    
    public TClient(Client client) {
        this.client = client;
        ci = new CommandInterpreter();
        start();
    }

    public boolean hasDataReceived() throws IOException, ClassNotFoundException{
        return getReceivedObject() != null;
    }
    
    public Client getClient(){
        return client;
    }

    public AccountManager getCloud(){
        return client.getAccountManager();
    }
    
    @Override
    public void sendObject(Object obj) throws IOException {
        client.getOutput().writeObject(obj);
        client.getOutput().flush();
    }

    @Override
    public Object getReceivedObject() throws IOException, ClassNotFoundException {
        Object received = null;
        do received = client.getInput().readObject();
        while(received == null);
        return received;
    }
    
    @Override
    public void run(){
        Object objReceived;
        TransferPackage transPack;
        Command cmd;
        User df;
        boolean operacionValida;
        
        while (true) {
            try {
                // Queda pendiente las operaciones con el cloud
                // ya que deben hacerse por comandos
                do objReceived = getReceivedObject();
                while (objReceived == null);
                
                if (objReceived instanceof TransferPackage) {
                    transPack = (TransferPackage) objReceived;
                    if (transPack.getCommand().isValid()) ci.execSpecialCommand(transPack, this);
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
    }
}
