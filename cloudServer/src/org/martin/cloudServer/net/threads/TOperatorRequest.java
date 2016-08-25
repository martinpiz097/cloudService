/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudServer.net.threads;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.martin.cloudCommon.interfaces.Receivable;
import org.martin.cloudCommon.interfaces.Transmissible;
import org.martin.cloudCommon.system.Command;
import org.martin.cloudServer.system.CommandInterpreter;

/**
 *
 * @author martin
 */
public class TOperatorRequest extends Thread implements Transmissible, Receivable {

    private final Socket sockRequest;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private final CommandInterpreter ci;
    
    public TOperatorRequest(Socket sockRequest) throws IOException {
        this.sockRequest = sockRequest;
        this.output = new ObjectOutputStream(this.sockRequest.getOutputStream());
        this.input = new ObjectInputStream(this.sockRequest.getInputStream());
        System.out.println("Operator request instanciado!");
        ci = new CommandInterpreter();
    }

    public Socket getSockRequest() {
        return sockRequest;
    }

    public ObjectInputStream getInput() {
        return input;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public void closeConnection() throws IOException {
        output.close();
        input.close();
        sockRequest.close();
    }

    public void closeStreams() throws IOException{
        output.close();
        input.close();
    }
    
    @Override
    public void sendObject(Object obj) throws IOException {
        output.writeObject(obj);
        output.flush();
    }

    @Override
    @SuppressWarnings("empty-statement")
    public Object getReceivedObject() throws IOException, ClassNotFoundException {
        return input.readObject();
    }

    @Override
    public void run() {
        Object objReceived = null;

        try {
            while (objReceived == null)       
                objReceived = getReceivedObject();
            
            System.out.println("Antes del if");
            if (objReceived instanceof Command){
                ci.execCommand((Command)objReceived, this);
                System.out.println("Respuesta enviada");
            }
            System.out.println("Al final del tor");
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(TOperatorRequest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
