/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudWebClient.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.martin.cloudCommon.interfaces.Receivable;
import org.martin.cloudCommon.interfaces.Transmissible;
import org.martin.cloudCommon.model.packages.TransferPackage;
import org.martin.cloudCommon.model.packages.UpdatePackage;
import org.martin.cloudCommon.system.Command;

/**
 *
 * @author martin
 */
public class Connector implements Transmissible, Receivable{
    private final Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    
    public Connector(Socket socket) throws IOException {
        this.socket = socket;
        this.output = new ObjectOutputStream(this.socket.getOutputStream());
        this.input = new ObjectInputStream(this.socket.getInputStream());
    }
    
    public void reinstanceOuputStream() throws IOException{
        output = new ObjectOutputStream(socket.getOutputStream());
    }
    
    public void reinstanceInputStream() throws IOException{
        input = new ObjectInputStream(socket.getInputStream());
    }
    
    public void reinstanceStreams() throws IOException{
        reinstanceOuputStream();
        reinstanceInputStream();
    }
    
    public void closeStreams() throws IOException{
        input.close();
        output.close();
        input = null;
        output = null;
    }
    
    public void closeConnection() throws IOException{
        closeStreams();
        socket.close();
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

    public void sendUpdateRequest(String currentDirPath, String nickUser) throws IOException{
        final Command cmdUpdate = new Command(Command.update.getOrder(), currentDirPath, nickUser);
        sendCommand(cmdUpdate);
    }
    
    public void sendCommand(String... strCommand) throws IOException{
        sendCommand(new Command(strCommand));
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
        Object objReceived = null;
        //while((objReceived = input.readObject()) == null);
        while (objReceived == null) objReceived = input.readObject();
        
        return objReceived;
    }
    
    public UpdatePackage getUpdatesReceived() throws IOException, ClassNotFoundException{
        final Object objReceived = getReceivedObject();
        
        if (objReceived instanceof UpdatePackage)
            return (UpdatePackage) objReceived;
        else
            return null;
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
