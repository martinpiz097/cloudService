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
    
    private final LinkedList<Archive> archives;
    private final LinkedList<Folder> folders;
    
    public Folder(LinkedList<Archive> archives, String pathname) {
        super(pathname);
        this.archives = archives;
        this.folders = new LinkedList<>();
    }

    public Folder(LinkedList<Archive> archives, String parent, String child) {
        super(parent, child);
        this.archives = archives;
        this.folders = new LinkedList<>();
    }

    public Folder(LinkedList<Archive> archives, File parent, String child) {
        super(parent, child);
        this.archives = archives;
        this.folders = new LinkedList<>();
    }

    public Folder(LinkedList<Archive> archives, URI uri) {
        super(uri);
        this.archives = archives;
        this.folders = new LinkedList<>();
    }

    public Folder(String pathname) {
        super(pathname);
        this.archives = new LinkedList<>();
        this.folders = new LinkedList<>();
    }

    public Folder(String parent, String child) {
        super(parent, child);
        this.archives = new LinkedList<>();
        this.folders = new LinkedList<>();
    }

    public Folder(File parent, String child) {
        super(parent, child);
        this.archives = new LinkedList<>();
        this.folders = new LinkedList<>();
    }

    public Folder(URI uri) {
        super(uri);
        this.archives = new LinkedList<>();
        this.folders = new LinkedList<>();
    }
    
    public boolean hasFolders(){
        return !folders.isEmpty();
    }
    
    public boolean hasArchives(){
        return !archives.isEmpty();
    }
    
    public void addArchive(Archive a){
        archives.add(a);
    }
    
    public void addFolder(Folder f){
        folders.add(f);
    }
    
    public int getListsCount(){
        return archives.size() + folders.size();
    }
    
    public LinkedList<Archive> getArchives() {
        return archives;
    }

    public LinkedList<Folder> getFolders() {
        return folders;
    }
    
    public void clearListArchives(){
        archives.clear();
    }
    
    public void clearListFolders(){
        folders.clear();
    }
    
}
