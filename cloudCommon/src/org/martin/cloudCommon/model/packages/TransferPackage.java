/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.model.packages;

import java.io.Serializable;
import org.martin.cloudCommon.system.Archive;
import org.martin.cloudCommon.system.Command;
import org.martin.cloudCommon.system.Folder;

/**
 *
 * @author martin
 */
public class TransferPackage implements Serializable{
    private final Command command;
    private final Archive archive;
    private final Folder folder;

    public TransferPackage(Command command, Archive archive, Folder folder) {
        this.command = command;
        this.archive = archive;
        this.folder = folder;
    }

    public TransferPackage(Command command, Archive archive) {
        this.command = command;
        this.archive = archive;
        this.folder = null;
    }

    public TransferPackage(Command command, Folder folder) {
        this.command = command;
        this.folder = folder;
        this.archive = null;
    }

    public TransferPackage(Command command) {
        this.command = command;
        this.folder = null;
        this.archive = null;
    }
    
    /**
     * Devuelve un boolean en funcion de que el paquete de transferencia
     * contenga un archivo o no.
     * @return true si el paquete contiene un archivo; false en caso contrario
     */
    
    public boolean hasArchive(){
        return archive != null;
    }
    
    /**
     * Devuelve un boolean en funcion de que el paquete de transferencia
     * contenga una carpeta o no.
     * @return true si el paquete contiene una carpeta; false en caso contrario
     */
    
    public boolean hasFolder(){
        return folder != null;
    }
    
    /**
     * Devuelve un boolean en funcion de que el paquete de transferencia
     * contenga solo el comando o no.
     * @return true si el paquete contiene solo el comando; false en caso contrario
     */
    
    public boolean hasOnlyCommand(){
        return !hasArchive() && !hasFolder();
    }

    /**
     * Retorna el comando del paquete
     * @return Objeto Command representando al comando contenido en el paquete
     */
    
    public Command getCommand() {
        return command;
    }
    
    /**
     * Retorna el archivo del paquete
     * @return Objeto Archive representando al archivo contenido en el paquete
     */

    public Archive getArchive() {
        return archive;
    }

    /**
     * Retorna la carpeta del paquete
     * @return Objeto Folder representando a la carpeta contenida en el paquete
     */
    
    public Folder getFolder() {
        return folder;
    }
    
    
}
