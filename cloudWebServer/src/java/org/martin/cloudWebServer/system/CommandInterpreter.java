/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and openConnection the template in the editor.
 */
package org.martin.cloudWebServer.system;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import org.martin.cloudCommon.model.User;
import org.martin.cloudCommon.model.packages.ClientPackage;
import org.martin.cloudCommon.model.packages.TransferPackage;
import org.martin.cloudCommon.model.packages.UpdatePackage;
import org.martin.cloudCommon.model.packages.UserPackage;
import org.martin.cloudCommon.system.Account;
import org.martin.cloudCommon.system.Command;
import org.martin.cloudWebServer.db.DbManager;
import org.martin.cloudWebServer.net.Client;
import org.martin.cloudWebServer.net.Server;
import org.martin.cloudWebServer.net.threads.TClient;
import org.martin.cloudWebServer.net.threads.TOperatorRequest;

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
            DbManager dbMan = new DbManager();
            User user = dbMan.getUserByNickAndPassw(firstOption, secondOption);
            if (user.isNull()) {
                operatorRequest.sendObject(new UserPackage(null));
                operatorRequest.closeConnection();
            }

            else{
                // Algoritmo que comprueba si el cliente ya esta o no conectado
//                boolean isConnected = Server.getInstance().isClientConnected(user.getId());
//                if (isConnected) {
//                    operatorRequest.sendObject(new UserPackage(null));
//                    operatorRequest.closeConnection();
//                }

                Account userAccount = dbMan.getAccountByUser(user.getId());
                operatorRequest.sendObject(new UserPackage(new ClientPackage(userAccount)));
                // Creando nuevos streams y olvidando los viejos ahorra algo de RAM
                // pero hay que averiguar en que afecta que estos streams queden en el olvido

                Server.getInstance().addClient(new Client(user, operatorRequest.getSockRequest(),
                        userAccount));
                
                userAccount = null;
            }
            dbMan.closeConnection();
            user = null;
        }
        else if (cmd.isEqualsOrder(Command.regU))
            operatorRequest.sendObject(Server.getInstance().addUser(firstOption, secondOption));
        
    }

    public void execCommand(Command cmd, TClient tClient) throws IOException, SQLException {
        final AccountManager clientCloud = tClient.getAccountManager();
        System.out.println("Cmd: " + cmd);
        System.out.println("Cantidad de opciones: " + cmd.getOptionsCount());
        
        final String firstOption = cmd.hasOptions() ? cmd.getFirstOption() : null;
        final String secondOption = cmd.hasOptions() ? cmd.getOptionAt(1) : null;
        
        if (cmd.isEqualsOrder(Command.access))
            tClient.sendObject(clientCloud.access(firstOption));
            
        else if (cmd.isEqualsOrder(Command.back))
            tClient.sendObject(clientCloud.back(firstOption));

        else if (cmd.isEqualsOrder(Command.close)) {
            tClient.closeConnection();
            Server.getInstance().removeClient(tClient.getClient().getIdUser());
            tClient = null;
        }
        
        else if (cmd.isEqualsOrder(Command.cpD))
            clientCloud.cpD(firstOption, secondOption);
        
        else if (cmd.isEqualsOrder(Command.cpF))
            clientCloud.cpF(firstOption, secondOption);
        
        else if (cmd.isEqualsOrder(Command.ctD))
            clientCloud.ctD(firstOption, secondOption);
        
        else if (cmd.isEqualsOrder(Command.ctF))
            clientCloud.ctF(firstOption, secondOption);
        
        else if (cmd.isEqualsOrder(Command.delD))
            clientCloud.delD(firstOption);
        
        else if (cmd.isEqualsOrder(Command.delF))
            clientCloud.delF(firstOption);
        
        else if (cmd.isEqualsOrder(Command.dwnD))
            tClient.sendObject(clientCloud.dwnD(firstOption, secondOption));
            
        else if (cmd.isEqualsOrder(Command.dwnF))
            tClient.sendObject(clientCloud.dwnF(firstOption, secondOption));
        
        else if (cmd.isEqualsOrder(Command.list))
            tClient.sendObject(clientCloud.list(firstOption));
        
        else if (cmd.isEqualsOrder(Command.loginU)){
            tClient.getDbManager().openConnection();
            final User user = tClient.getDbManager()
                    .getUserByNickAndPassw(firstOption, secondOption);
            // Si se envia un usuario que no es nulo significa que esta correcto el login
            tClient.sendObject(user);
            tClient.getDbManager().closeConnection();
        }

        else if (cmd.isEqualsOrder(Command.mkd))
            tClient.getAccountManager().mkd(firstOption, secondOption);

        else if (cmd.isEqualsOrder(Command.regU))
            tClient.sendObject(Server.getInstance().addUser(firstOption, secondOption));
        
        else if (cmd.isEqualsOrder(Command.rename))
            tClient.getAccountManager().rename(firstOption, secondOption);
        
        else if (cmd.isEqualsOrder(Command.root))
            tClient.sendObject(clientCloud.root());
    
        else if (cmd.isEqualsOrder(Command.update)) {
            tClient.getDbManager().openConnection();
            final File curDir = tClient.getAccountManager().access(firstOption);
            final Account account = tClient.getDbManager().getAccountByUser(
                    tClient.getClient().getIdUser());
            
            tClient.sendObject(new UpdatePackage(curDir, account));
            tClient.getDbManager().closeConnection();
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
    
    public void execParallelCommand(TransferPackage tp, TClient tClient) throws SQLException, IOException{
        final AccountManager ac = new AccountManager(
                tClient.getDbManager().getAccountByUser(tClient.getClient().getIdUser()));
    
        tClient.getDbManager().closeConnection();
        if (tp.getCommand().isEqualsOrder(Command.uplD))
            ac.uplD(tp.getFolder());
            
        else if (tp.getCommand().isEqualsOrder(Command.uplF)){
            System.out.println("Es uplF");
            ac.uplF(tp.getCommand().getOptionAt(1), tp.getArchive());
        }
    }
}
