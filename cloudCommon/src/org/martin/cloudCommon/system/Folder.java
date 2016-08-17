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
public class Folder extends File{
    
    private final LinkedList<File> archives;

    public Folder(LinkedList<File> archives, String pathname) {
        super(pathname);
        this.archives = archives;
    }

    public Folder(LinkedList<File> archives, String parent, String child) {
        super(parent, child);
        this.archives = archives;
    }

    public Folder(LinkedList<File> archives, File parent, String child) {
        super(parent, child);
        this.archives = archives;
    }

    public Folder(LinkedList<File> archives, URI uri) {
        super(uri);
        this.archives = archives;
    }

    public Folder(String pathname) {
        super(pathname);
        this.archives = new LinkedList<>();
    }

    public Folder(String parent, String child) {
        super(parent, child);
        this.archives = new LinkedList<>();
    }

    public Folder(File parent, String child) {
        super(parent, child);
        this.archives = new LinkedList<>();
    }

    public Folder(URI uri) {
        super(uri);
        this.archives = new LinkedList<>();
    }
    
    public void addArchive(Archive a){
        archives.add(a);
    }
    
    public void addFolder(Folder f){
        archives.add(f);
    }
    
    public LinkedList<File> getAll() {
        return archives;
    }
    
    public LinkedList<Folder> getFolders(){
        LinkedList<Folder> folders = new LinkedList<>();
    }
    public void clear(){
        archives.clear();
    }
    
}
