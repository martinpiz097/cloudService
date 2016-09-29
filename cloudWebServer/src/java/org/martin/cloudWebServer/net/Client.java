/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudWebServer.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import org.martin.cloudCommon.model.User;
import org.martin.cloudCommon.system.Account;
import org.martin.cloudWebServer.system.AccountManager;

/**
 *
 * @author martin
 */
public class Client {
    private final long idUser;
    private final User user;
    private final AccountManager accountManager;
    private final Socket socket;
    private final ObjectOutputStream output;
    private final ObjectInputStream input;

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
    
    public void closeStreams() throws IOException{
        input.close();
        output.close();
    }
    
    public void closeConnection() throws IOException{
        closeStreams();
        socket.close();
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
