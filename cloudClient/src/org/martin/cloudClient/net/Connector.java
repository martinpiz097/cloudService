/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudClient.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.martin.cloudCommon.interfaces.Receivable;
import org.martin.cloudCommon.interfaces.Transmissible;
import org.martin.cloudCommon.model.packages.TransferPackage;
import org.martin.cloudCommon.system.Command;

/**
 *
 * @author martin
 */
public class Connector implements Transmissible, Receivable{
    private final Socket socket;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;
    
    public Connector(Socket socket) throws IOException {
        this.socket = socket;
        output = new ObjectOutputStream(this.socket.getOutputStream());
        input = new ObjectInputStream(this.socket.getInputStream());
    }
    
    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public ObjectInputStream getInput() {
        return input;
    }

    public void sendCommand(Command cmd) throws IOException{
        sendObject(cmd);
    }
    
    public void sendTransferPackage(TransferPackage tp) throws IOException{
        sendObject(tp);
    }
    
    @Override
    public void sendObject(Object obj) throws IOException {
        output.writeObject(obj);
        output.flush();
    }

    /**
     * En el cliente el método getReceivedObject asegura que el objeto entregado no
     * es nulo, creando un loop hasta que llegue un objeto y poder entregarlo por el
     * método
     * @return Objeto recibido desde el servidor
     * @throws IOException En caso de problemas al recibir el objeto
     * @throws ClassNotFoundException En caso de problemas de casteo de clases.
     */
    
    @Override
    @SuppressWarnings("empty-statement")
    public Object getReceivedObject() throws IOException, ClassNotFoundException {
        Object objReceived;
        while((objReceived = input.readObject()) == null);
        return objReceived;
    }
    
//    private final User user;
//
//    public Connector(User user, Socket socket) throws IOException {
//        super(socket);
//        this.user = user;
//    }
//    
//    public Connector(User user, DefaultConnector df){
//        super(df.getSocket());
//    }
    
}
