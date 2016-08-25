/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudServer.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.martin.cloudCommon.model.User;
import org.martin.cloudCommon.system.Cloud;
import org.martin.cloudServer.net.threads.TOperatorRequest;

/**
 *
 * @author martin
 */
public class Client {
    
    private final int idUser;
    private final User user;
    private final Socket socket;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;

    public Client(User user, Socket socket) throws IOException {
        this.idUser = user.getId();
        this.user = user;
        this.socket = socket;
        this.output = new ObjectOutputStream(this.socket.getOutputStream());
        this.input = new ObjectInputStream(this.socket.getInputStream());
    }

    public Client(User user, Socket socket, ObjectOutputStream output, ObjectInputStream input) {
        this.idUser = user.getId();
        this.user = user;
        this.socket = socket;
        this.output = output;
        this.input = input;
    }
    
    public int getIdUser() {
        return idUser;
    }

    public User getUser() {
        return user;
    }

    public Cloud getCloud(){
        return user.getCloud();
    }
    
    public ObjectOutputStream getOutput() {
        return output;
    }

    public ObjectInputStream getInput() {
        return input;
    }
    
}
