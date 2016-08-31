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

/**
 *
 * @author martin
 */
public class CloudInfo implements Serializable{
    
    // Todos los espacios son en bytes
    private long usedSpace;
    private long totalSpace;
    private final Date creationDate;
    private static transient Calendar c;

    public CloudInfo(long totalSpace) {
        this.usedSpace = 0;
        this.totalSpace = totalSpace;
        c = new GregorianCalendar();
        creationDate = c.getTime();
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

    public long getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(long totalSpace) {
        this.totalSpace = totalSpace;
    }

    public Date getCreationDate() {
        return creationDate;
    }
    
}
