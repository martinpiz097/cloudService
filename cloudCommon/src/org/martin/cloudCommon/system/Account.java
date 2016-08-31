/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.system;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import org.martin.cloudCommon.model.User;

/**
 *
 * @author martin
 */
public class Account implements Serializable{
    
    // Todos los espacios son en bytes
    private final long id;
    private long usedSpace;
    private final User idUser;
    private final String rootDirName;
    private long totalSpace;
    private final Date creationDate;
    private static transient Calendar c;

    public Account(long id, User user, String rootDirName, long totalSpace) {
        this.id = id;
        this.usedSpace = 0;
        this.idUser = user;
        this.rootDirName = rootDirName;
        this.totalSpace = totalSpace;
        c = new GregorianCalendar();
        creationDate = c.getTime();
    }

    public Account(long id, long usedSpace, User user, String rootDirName, long totalSpace, Date creationDate) {
        this.id = id;
        this.usedSpace = usedSpace;
        this.idUser = user;
        this.rootDirName = rootDirName;
        this.totalSpace = totalSpace;
        this.creationDate = creationDate;
        c = new GregorianCalendar();
    }

    public long getId() {
        return id;
    }

    public long getUsedSpace() {
        return usedSpace;
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

    public User getIdUser() {
        return idUser;
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
