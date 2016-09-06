/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudClient.model;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import javax.swing.DefaultListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author martin
 */
public class LMDirectories extends DefaultListModel<String> implements ListModel<String>{
    
    private File[] directories;
    
    public LMDirectories(File directory) {
        if (directory != null){
            File[] dirs = directory.listFiles(File::isDirectory);
            if (dirs != null) {
                directories = dirs;
                Arrays.sort(dirs, File::compareTo);
            }
            else
                directories = null;
        }
        else
            directories = null;
    }
    
    public void setDirectories(File directory){
        directories = directory.listFiles(File::isDirectory);
    }
    
    @Override
    public int getSize() {
        return directories == null ? 0 : directories.length;
    }

    @Override
    public String getElementAt(int index) {
        return directories == null ? null : 
                (directories.length == 0 ? null : directories[index].getName());
    }

    @Override
    public void addListDataListener(ListDataListener l) {
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
    }
    
}
