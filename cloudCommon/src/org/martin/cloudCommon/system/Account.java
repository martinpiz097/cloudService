/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.system;

import java.io.Serializable;
import java.util.Date;
import org.martin.cloudCommon.model.User;

/**
 *
 * @author martin
 */
public class Account implements Serializable{
    
    // Todos los espacios son en bytes
    private final long id;
    private long usedSpace;
    private final User user;
    private final String rootDirName;
    private long totalSpace;
    private final Date creationDate;
    //private static transient Calendar c;

    // Constructor por defecto para añadir cuentas a la db
    public Account(User user, long totalSpace){
        this.id = -1;
        this.usedSpace = 0;
        this.user = user;
        this.rootDirName = SysManager.getRootDirectoryName(user);
        this.totalSpace = totalSpace;
        this.creationDate = null;
    }
    
    public Account(long id, User user, String rootDirName, long totalSpace) {
        this.id = id;
        this.usedSpace = 0;
        this.user = user;
        this.rootDirName = rootDirName;
        this.totalSpace = totalSpace;
        this.creationDate = null;
    }

    public Account(long id, User user, String rootDirName, long usedSpace, long totalSpace, Date creationDate) {
        this.id = id;
        this.usedSpace = usedSpace;
        this.user = user;
        this.rootDirName = rootDirName;
        this.totalSpace = totalSpace;
        this.creationDate = creationDate;
    }

    public long getId() {
        return id;
    }

    public long getUsedSpace() {
        return usedSpace;
    }   

    public long getFreeSpace(){
        System.out.println("Espacion total: "+getTotalSpace());
        System.out.println("Espacio usado: "+getUsedSpace());
        return getTotalSpace()-getUsedSpace();
    }
    
    public void setUsedSpace(long usedSpace) {
        this.usedSpace = usedSpace;
    }
    
    public void addUsedSpace(long fileLenght){
        this.usedSpace+=fileLenght;
    }
    
    public void removeUsedSpace(long fileLenght){
        this.usedSpace-=fileLenght;
    }

    public User getUser() {
        return user;
    }

    public String getRootDirName() {
        return rootDirName;
    }

    public long getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(long totalSpace) {
        this.totalSpace = totalSpace;
    }
    
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public String toString() {
        return "CloudInfo{" + "usedSpace=" + usedSpace + ", totalSpace=" + totalSpace + ", creationDate=" + creationDate + '}';
    }

    
}
