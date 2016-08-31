/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudServer.system;

import java.io.IOException;
import java.sql.SQLException;
import org.martin.cloudCommon.model.User;
import org.martin.cloudCommon.model.packages.ClientPackage;
import org.martin.cloudCommon.model.packages.TransferPackage;
import org.martin.cloudCommon.model.packages.UserPackage;
import org.martin.cloudCommon.system.Account;
import org.martin.cloudCommon.system.Command;
import org.martin.cloudServer.db.DbManager;
import org.martin.cloudServer.net.Client;
import org.martin.cloudServer.net.Server;
import org.martin.cloudServer.net.threads.TClient;
import org.martin.cloudServer.net.threads.TOperatorRequest;

/**
 *
 * @author martin
 */
public class CommandInterpreter {
    
    // Mas adelante enviar codigos por cada operacion realizada(puede ser un objeto
    // o desde una enum)

    // Se debe tener como objeto ya que debe ser un interprete por cada hilo
    // para evitar las sincronizaciones

    public CommandInterpreter(){}
    
    public void execCommand(Command cmd, TOperatorRequest operatorRequest) throws IOException, SQLException{
        final String firstOption = cmd.getFirstOption();
        final String secondOption = cmd.getOption(1);
        
        if (cmd.isEqualsOrder(Command.loginU)){
            final User user = Server.getInstance().getUser(firstOption, secondOption);
            if (user.isNull()) {
                operatorRequest.sendObject(new UserPackage(null));
                operatorRequest.closeConnection();
            }
            else{
                final DbManager xpressConnection = new DbManager();
                final Account userAccount = xpressConnection.getAccountByUser(user.getId());
                operatorRequest.sendObject(new UserPackage(new ClientPackage(userAccount)));
                Server.getInstance().addClient(new Client(
                        user, operatorRequest.getSockRequest(), userAccount));
                
                operatorRequest.closeStreams();
            }
        }
        else if (cmd.isEqualsOrder(Command.regU))
            operatorRequest.sendObject(Server.getInstance().addUser(firstOption, secondOption));
        
    }

    public void execCommand(Command cmd, TClient tClient) throws IOException, SQLException {
        final AccountManager clientCloud = tClient.getAccountManager();
        final String firstOption = cmd.getFirstOption();
        final String secondOption = cmd.getOption(1);
        
        if (cmd.isEqualsOrder(Command.access))
            tClient.sendObject(clientCloud.access(firstOption));
            
        else if (cmd.isEqualsOrder(Command.back))
            tClient.sendObject(clientCloud.back(firstOption));
        
//        else if (cmd.isEqualsOrder(Command.cpD))
//            clientCloud.cpD(firstOption, secondOption);
        
        else if (cmd.isEqualsOrder(Command.cpF))
            clientCloud.cpF(firstOption, secondOption);
        
//        else if (cmd.isEqualsOrder(Command.ctD)) {
//
//        }
        else if (cmd.isEqualsOrder(Command.ctF))
            clientCloud.ctF(firstOption, secondOption);
        
        else if (cmd.isEqualsOrder(Command.delD))
            clientCloud.delD(firstOption);
        
        else if (cmd.isEqualsOrder(Command.delF))
            clientCloud.delF(firstOption);
        
//        else if (cmd.isEqualsOrder(Command.dwnD))
        
        
        else if (cmd.isEqualsOrder(Command.dwnF))
            tClient.sendObject(clientCloud.dwnF(firstOption, secondOption));
        
        else if (cmd.isEqualsOrder(Command.list))
            tClient.sendObject(clientCloud.list(firstOption));
        
        else if (cmd.isEqualsOrder(Command.loginU)){
            final User user = Server.getInstance().getUser(firstOption, secondOption);
            // Si recibe un usuario que no es null significa que esta correcto el login
            tClient.sendObject(user);
            
        }
        else if (cmd.isEqualsOrder(Command.regU))
            tClient.sendObject(Server.getInstance().addUser(firstOption, secondOption));
        
        else if (cmd.isEqualsOrder(Command.root))
            tClient.sendObject(clientCloud.root());
    }
    
    public void execSpecialCommand(TransferPackage tp, TClient tClient) throws IOException, SQLException{

        // Revisar la logica del codigo
        if (tp.getCommand().isEqualsOrder(Command.uplD))
            tClient.getAccountManager().uplD(tp.getFolder());
            
        else if (tp.getCommand().isEqualsOrder(Command.uplF))
            tClient.getAccountManager().uplF(tp.getArchive());
        
    }
}
