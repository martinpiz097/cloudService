/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.system;

import java.io.File;
import java.net.URI;
import java.util.LinkedList;

/**
 *
 * @author martin
 */
public class Archive extends File{
    
    private final LinkedList<byte[]> queueBytes;

    public Archive(String pathname) {
        super(pathname);
        this.queueBytes = new LinkedList<>();
    }

    public Archive(String parent, String child) {
        super(parent, child);
        this.queueBytes = new LinkedList<>();
    }

    public Archive(File parent, String child) {
        super(parent, child);
        this.queueBytes = new LinkedList<>();
    }

    public Archive(URI uri) {
        super(uri);
        this.queueBytes = new LinkedList<>();
    }

    public void addByte(byte[] b){
        queueBytes.add(b);
    }
    
    public LinkedList<byte[]> getQueueBytes() {
        return queueBytes;
    }
    
    public void clear(){
        queueBytes.clear();
    }
    
}
