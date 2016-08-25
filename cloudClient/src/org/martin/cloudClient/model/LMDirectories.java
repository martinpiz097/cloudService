/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudClient.model;

import java.io.File;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author martin
 */
public class LMDirectories implements ListModel<File>{
    
    private File[] directories;
    
    public LMDirectories(File directory) {
        if (directory != null) 
            directories = directory.listFiles(File::isDirectory);
        
    }
    
    public void setDirectories(File directory){
        directories = directory.listFiles(File::isDirectory);
    }

    @Override
    public int getSize() {
        return directories.length;
    }

    @Override
    public File getElementAt(int index) {
        return directories[index];
    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }
    
}
