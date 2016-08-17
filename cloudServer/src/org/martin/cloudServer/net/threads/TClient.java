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
import org.martin.cloudCommon.system.Cloud;
import org.martin.cloudServer.net.Client;

/**
 *
 * @author martin
 */
public class TClient extends Thread implements Transmissible, Receivable{
    
    private final Client client;

    public TClient(Client client) {
        this.client = client;
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
        return client.getInput().readObject();
    }
    
    @Override
    public void run(){
        while (true) {            
            try {
                // Queda pendiente las operaciones con el cloud
                // ya que deben hacerse por comandos
                Thread.sleep(300);
            } catch (InterruptedException ex) {
                Logger.getLogger(TClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
