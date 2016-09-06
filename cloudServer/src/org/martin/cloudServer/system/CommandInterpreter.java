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
        final String secondOption = cmd.getOptionAt(1);
        
        if (cmd.isEqualsOrder(Command.loginU)){
            final User user = Server.getInstance().getUser(firstOption, secondOption);
            if (user.isNull()) {
                operatorRequest.sendObject(new UserPackage(null));
                operatorRequest.closeConnection();
            }

            else{
                // Algoritmo que comprueba si el cliente esta o no conectado
//                boolean isConnected = Server.getInstance().isClientConnected(user.getId());
//                if (isConnected) {
//                    operatorRequest.sendObject(new UserPackage(null));
//                    operatorRequest.closeConnection();
//                }

                DbManager xpressConnection = new DbManager();
                Account userAccount = xpressConnection.getAccountByUser(user.getId());
                operatorRequest.sendObject(new UserPackage(new ClientPackage(userAccount)));
                // Creando nuevos streams y olvidando los viejos ahorra algo de RAM
                // pero hay que averiguar en que afecta que estos streams queden en el olvido

                Server.getInstance().addClient(new Client(user, operatorRequest.getSockRequest(),
                        userAccount));
            }
        }
        else if (cmd.isEqualsOrder(Command.regU))
            operatorRequest.sendObject(Server.getInstance().addUser(firstOption, secondOption));
        
    }

    public void execCommand(Command cmd, TClient tClient) throws IOException, SQLException {
        final AccountManager clientCloud = tClient.getAccountManager();
        System.out.println("Cmd: " + cmd);
        System.out.println("Cantidad de opciones: " + cmd.getOptionsCount());
        
        String firstOption = cmd.hasOptions() ? cmd.getFirstOption() : null;
        String secondOption = cmd.hasOptions() ? cmd.getOptionAt(1) : null;
        
        if (cmd.isEqualsOrder(Command.access))
            tClient.sendObject(clientCloud.access(firstOption));
            
        else if (cmd.isEqualsOrder(Command.back))
            tClient.sendObject(clientCloud.back(firstOption));

        else if (cmd.isEqualsOrder(Command.close)) {
            tClient.closeConnection();
            Server.getInstance().removeClient(tClient.getClient().getIdUser());
            tClient = null;
        }
        
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
    
        else if (cmd.isEqualsOrder(Command.mkd)){
            System.out.println("Es mkd");
            tClient.getAccountManager().mkd(firstOption, secondOption);
        } 
        
    }
    
    public void execSpecialCommand(TransferPackage tp, TClient tClient) throws IOException, SQLException{

        System.out.println("Estamos en el execSpecialCommand;cmd: "+tp.getCommand());
        // Revisar la logica del codigo
        if (tp.getCommand().isEqualsOrder(Command.uplD))
            tClient.getAccountManager().uplD(tp.getFolder());
            
        else if (tp.getCommand().isEqualsOrder(Command.uplF)){
            System.out.println("Es uplF");
            tClient.getAccountManager().uplF(
                    tp.getCommand().getOptionAt(1), tp.getArchive());
        }
            
        
    }
}
