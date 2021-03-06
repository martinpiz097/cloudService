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
import java.sql.SQLException;
import org.martin.cloudCommon.model.User;
import org.martin.cloudCommon.system.Account;
import org.martin.cloudServer.system.AccountManager;

/**
 *
 * @author martin
 */
public class Client {
    private final long idUser;
    private User user;
    private AccountManager accountManager;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public Client(User user, Socket socket, Account account) throws IOException, SQLException {
        this.idUser = user.getId();
        this.user = user;
        this.accountManager = new AccountManager(account);
        this.socket = socket;
        this.output = new ObjectOutputStream(this.socket.getOutputStream());
        this.input = new ObjectInputStream(this.socket.getInputStream());
    }

    public Client(User user, Socket socket, Account account, ObjectOutputStream output, ObjectInputStream input) throws SQLException, IOException {
        this.idUser = user.getId();
        this.user = user;
        this.accountManager = new AccountManager(account);
        this.socket = socket;
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
        this.output = output;
        this.input = input;
    }
    
    private void cancelAll(){
        user = null;
        accountManager = null;
    }
    
    public void closeSocket() throws IOException{
        socket.close();
        socket = null;
    }
    
    public void closeStreams() throws IOException{
        input.close();
        input = null;
        output.close();
        output = null;
    }
    
    public void closeConnection() throws IOException{
        closeStreams();
        closeSocket();
        cancelAll();
    }
    
    public long getIdUser() {
        return idUser;
    }

    public User getUser() {
        return user;
    }

    public AccountManager getAccountManager(){
        return accountManager;
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
    
}
