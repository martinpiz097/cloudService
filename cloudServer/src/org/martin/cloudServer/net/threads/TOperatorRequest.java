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
import org.martin.cloudCommon.model.DefaultUser;
import org.martin.cloudCommon.model.User;
import org.martin.cloudCommon.model.packages.UserPackage;
import org.martin.cloudServer.db.DbManager;
import org.martin.cloudServer.net.Client;
import org.martin.cloudServer.net.Server;

/**
 *
 * @author martin
 */
public class TOperatorRequest extends Thread implements Transmissible, Receivable {

    private final Socket sockRequest;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;

    public TOperatorRequest(Socket sockRequest) throws IOException {
        this.sockRequest = sockRequest;
        this.output = new ObjectOutputStream(this.sockRequest.getOutputStream());
        this.input = new ObjectInputStream(this.sockRequest.getInputStream());
    }

    public void closeConnection() throws IOException {
        output.close();
        input.close();
        sockRequest.close();
    }

    @Override
    public void sendObject(Object obj) throws IOException {
        output.writeObject(obj);
        output.flush();
    }

    @Override
    public Object getReceivedObject() throws IOException, ClassNotFoundException {
        return input.readObject();
    }

    @Override
    public void run() {
        Object objReceived = null;
        UserPackage upReceived;
        boolean isValidRequest;
        User user;

        try {
            do objReceived = getReceivedObject();
            while (objReceived == null);

            if (objReceived instanceof UserPackage) {
                upReceived = (UserPackage) objReceived;
                if (upReceived.isLogin()) {
                    isValidRequest = Server.getInstance().isValidUser(upReceived.getUser());
                    if (isValidRequest) {
                        user = Server.getInstance().getUser(upReceived.getNick());
                        Server.getInstance().addClient(new Client(user, sockRequest, output, input));
                    }
                    sendObject(isValidRequest);
                }
                else {
                    isValidRequest = Server
                            .getInstance().isValidRegistration(upReceived.getUser());
                    if (isValidRequest)
                        Server.getInstance().addUser(upReceived.getUser());
                    
                    sendObject(isValidRequest);
                }
            }
            closeConnection();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(TOperatorRequest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
