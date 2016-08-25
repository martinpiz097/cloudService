/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudServer.net.threads;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.martin.cloudCommon.interfaces.Receivable;
import org.martin.cloudCommon.interfaces.Transmissible;
import org.martin.cloudCommon.model.DefaultUser;
import org.martin.cloudCommon.model.packages.TransferPackage;
import org.martin.cloudCommon.system.Cloud;
import org.martin.cloudCommon.system.Command;
import org.martin.cloudCommon.systemMessages.ServerResponses;
import org.martin.cloudServer.net.Client;
import org.martin.cloudServer.net.Server;
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

    public Cloud getCloud(){
        return client.getCloud();
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
        TransferPackage tp;
        Command cmd;
        DefaultUser df;
        boolean operacionValida;
        
        while (true) {
            try {
                // Queda pendiente las operaciones con el cloud
                // ya que deben hacerse por comandos
                do objReceived = getReceivedObject();
                while (objReceived == null);
                
                if (objReceived instanceof TransferPackage) {
                    tp = (TransferPackage) objReceived;
                    if (tp.getCommand().isValid()) ci.execSpecialCommand(tp, this);
                }
                else if (objReceived instanceof Command) {
                    cmd = (Command) objReceived;
                    if (cmd.isValid()) ci.execCommand(cmd, this);
                }
                
                Thread.sleep(300);
            } catch (InterruptedException | IOException | ClassNotFoundException ex) {
                Logger.getLogger(TClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
