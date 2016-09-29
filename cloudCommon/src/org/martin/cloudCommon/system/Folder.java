/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.system;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.martin.cloudCommon.interfaces.BinSerializable;

/**
 *
 * @author martin
 */
public final class Folder extends File implements BinSerializable{
    
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
    
    public Folder(String parent, String child, boolean create) throws IOException{
        this(parent, child);
        if (create) writeBytesFrom(this);
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

    public boolean isEmpty(){
        return !hasArchives() && !hasFolders();
    }
    
    // Metodo utilizado en el caso de que se cree un objeto
    // Folder solo como representacion logica, no fisica.
    public void move(File newParent) throws IOException{
        if (newParent == null || !newParent.exists()) return;
        if(newParent.getCanonicalPath().equals(getCanonicalPath())) return;
        
        File newFld = new File(newParent, getName());
        newFld.mkdir();
        
        for (Folder fld : folders) {
            fld.read();
            fld.writeBytesTo(new File(newFld, fld.getName()));
            fld.remove();
            
        }
        
        for (Archive archive : archives) {
            archive.read();
            archive.writeBytesTo(new File(newFld, archive.getName()));
            archive.delete();
        }
    }
    
    public void create() throws IOException{
        writeBytesTo(this);
    }

    public void createFrom(File origin) throws IOException{
        writeBytesFrom(origin);
        create();
    }
    
    public void remove(){
        if(!isEmpty()){
            archives.stream().forEach(File::delete);
            folders.stream().forEach(Folder::remove);
        }
        this.delete();
    }
    
    public void read() throws IOException{
        writeBytesFrom(this);
    }
    
    @Override
    public void writeBytesFrom(String path) throws IOException {
        writeBytesFrom(new File(path));
    }

    @Override
    public void writeBytesFrom(File origin) throws IOException {
        final File[] files = origin.listFiles();
        if(files == null || files.length == 0) return;
        
        Archive archive;
        Folder folder;
        
        for (File file : files) {
            if (file.isDirectory()) {
                folder = new Folder(file.getCanonicalPath());
                folder.writeBytesFrom(file);
                folders.add(folder);
            }
            else{
                archive = new Archive(file.getCanonicalPath());
                archive.writeBytesFrom(file);
                archives.add(archive);
            }
        }
    }

    @Override
    public void writeBytesTo(String path) throws IOException {
        writeBytesTo(new File(path));
    }
    

    @Override
    public void writeBytesTo(File destiny) throws IOException {
        destiny.mkdir();
        for (Folder fld : folders)
            fld.writeBytesTo(new File(destiny, fld.getName()));
        
        for (Archive archive : archives) 
            archive.writeBytesTo(new File(destiny, archive.getName()));
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
