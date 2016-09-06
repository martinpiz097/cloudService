/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import org.martin.cloudCommon.interfaces.BinSerializable;

/**
 *
 * @author martin
 */
public class Archive extends File implements BinSerializable{
    
    private final LinkedList<byte[]> queueBytes;
    private transient FileInputStream reader;
    private transient FileOutputStream writer;
    
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

    public void create() throws IOException{
        writeBytesTo(this);
    }
    
    @Override
    public void writeBytesFrom(String path) throws IOException{
        writeBytesFrom(new File(path));
    }
    
    @Override
    public void writeBytesFrom(File origin) throws IOException{
        reader = new FileInputStream(origin);
        
        System.err.println("Estoy dentro del writeBytesFrom; origin: "+origin.getCanonicalPath());
        byte[] buff = new byte[(int)origin.length()];
        reader.read(buff);
        addBytes(buff);
        reader.close();
        System.err.println("Bytes escritos dentro del archivo");
    }
    
    @Override
    public void writeBytesTo(String path) throws IOException{
        writeBytesTo(new File(path));
    }
    
    @Override
    public void writeBytesTo(File destiny) throws IOException{
        if (!destiny.exists()) destiny.createNewFile();
        
        writer = new FileOutputStream(destiny);
        writer.write(queueBytes.poll());
        writer.close();
    }
    
    public void addBytes(byte[] b){
        queueBytes.add(b);
    }
    
    public LinkedList<byte[]> getQueueBytes() {
        return queueBytes;
    }
    
    public void clear(){
        queueBytes.clear();
    }
    
}
