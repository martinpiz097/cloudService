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
import java.sql.SQLException;
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
public class TOperatorRequest implements Runnable, Transmissible, Receivable {
    private Socket sockRequest;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private CommandInterpreter ci;
    
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
        closeStreams();
        sockRequest.close();
        sockRequest = null;
    }

    public void closeStreams() throws IOException{
        output.close();
        input.close();
        output = null;
        input = null;
    }
    
    @Override
    public void sendObject(Object obj) throws IOException {
        output.writeObject(obj);
    }

    @Override

    public Object getReceivedObject() throws IOException, ClassNotFoundException {
        return input.readObject();
    }

    @Override
    public void run() {
        Object objReceived = null;
        long currentTime = System.currentTimeMillis();
        
        try {
            while (objReceived == null){
                if ((System.currentTimeMillis()-currentTime)/1000 >= 10)
                    break;
                objReceived = getReceivedObject();
            }
            
            if (objReceived == null) {
                System.out.println("No llegó ningun objeto");
                closeConnection();
            }
            
            else {
                System.out.println("Antes del if");
                if (objReceived instanceof Command){
                    ci.execCommand((Command)objReceived, this);
                    System.out.println("Respuesta enviada");
                }
                objReceived = null;
                output = null;
                input = null;
            }
            
            currentTime = 0;
            ci = null;
            System.out.println("Al final del tor");
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(TOperatorRequest.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            try {
                closeConnection();
                //Logger.getLogger(TOperatorRequest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex1) {
            }
        }

    }
}
