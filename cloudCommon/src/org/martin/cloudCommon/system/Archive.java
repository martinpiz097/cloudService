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
import java.util.Arrays;
import java.util.LinkedList;
import org.martin.cloudCommon.interfaces.BinSerializable;

/**
 *
 * @author martin
 */
public final class Archive extends File implements BinSerializable{
    
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
    
    public void createFrom(File origin) throws IOException{
        writeBytesFrom(origin);
        create();
    }
    
    public void read() throws IOException{
        writeBytesFrom(this);
    }

    public void readFromXML(String content){
        if(content ==  null) return;
        
        content = content.trim();
        String[] splitBytes;
        byte[] bytes;
        
        if (content.contains("@")) {
            String[] split = content.split("@");
            
            for (String str : split) {
                splitBytes = str.substring(1, str.length()-1).split(", ");
                bytes = new byte[splitBytes.length];
        
                for (int i = 0; i < splitBytes.length; i++)
                    bytes[i] = Byte.parseByte(splitBytes[i]);
            
                addBytes(bytes);
            }
            split = null;
        }
        else{
            splitBytes = content.substring(1, content.length()-1).split(", ");
            bytes = new byte[splitBytes.length];
        
            for (int i = 0; i < splitBytes.length; i++)
                bytes[i] = Byte.parseByte(splitBytes[i]);
            
            addBytes(bytes);
        }
        
        bytes = null;
        splitBytes = null;
    }
    
    public boolean renameTo(String newName) throws IOException{
        Archive file = new Archive(getParent(), newName);
        boolean renamed = !file.exists();

        if (!renamed) return renamed;
            
        else{
            this.clear();
            this.read();
            file.createFrom(this);
            this.delete();
        }
        
        return renamed;
    }
    
    public String writeToXML(){
        String bytes = "";
        bytes = queueBytes.stream()
                .map((b) -> (Arrays.toString(b)+"@"))
                .reduce(bytes, String::concat);
        
        bytes = bytes.substring(0, bytes.length()-1);
        return bytes;
    }
    
    @Override
    public void writeBytesFrom(String path) throws IOException{
        writeBytesFrom(new File(path));
    }

    public void writeBytesFrom(byte[] bytes){
        addBytes(bytes);
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
        destiny.createNewFile();
        
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
