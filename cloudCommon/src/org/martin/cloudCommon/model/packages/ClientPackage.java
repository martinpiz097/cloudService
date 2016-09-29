/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.model.packages;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.martin.cloudCommon.model.User;
import org.martin.cloudCommon.system.Account;
import org.martin.cloudCommon.system.SysInfo;
import org.martin.cloudCommon.system.Utilities;

/**
 *
 * @author martin
 */
public class ClientPackage implements Serializable{
    private Account account;
    private File currentDir;
    private String formattedCurDir;
    
    public static ClientPackage newInstance(Account account, File currentDir){
        try {
            return new ClientPackage(account);
        } catch (IOException ex) {
            Logger.getLogger(ClientPackage.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public ClientPackage(Account account) throws IOException {
        this.account = account;
        this.currentDir = new File(SysInfo.ROOT_FOLDER_NAME, account.getRootDirName());
        System.out.println("Existe: "+currentDir.exists());
        System.out.println("Es directorio: "+currentDir.isDirectory());
        if (currentDir.listFiles() != null) {
            for (File file : currentDir.listFiles())
                System.out.println(file.getName());
        }
        
        // Por sea caso
        if(!this.currentDir.exists()) currentDir.mkdir();
        createFormattedCurrentDir();
    }
    
    public void update(UpdatePackage up) throws IOException{
        this.account = up.getAccount();
        this.currentDir = up.getCurrentDir();
        up = null;
        createFormattedCurrentDir();
    }

    public User getUser() {
        return account.getUser();
    }

    public String getUserNick(){
        return account.getUser().getNick();
    }
    
    public String getUserPassword(){
        return account.getUser().getPassword();
    }

    public double getUsedSpaceInMB(){
        return (double)account.getUsedSpace()/(1024*1024);
    }
    
    public double getFreeSpaceInMB(){
        return (double)account.getFreeSpace()/(1024*1024);
    }

    public double getTotalSpaceInMB(){
        return (double)account.getTotalSpace()/(1024*1024);
    }
    
    public String getDateToString(){
        return Utilities.getDateToString(account.getCreationDate());
    }
    
    public Account getAccount() {
        return account;
    }

    public String getFormattedCurrentDir() {
        return formattedCurDir;
    }
    
    public void createFormattedCurrentDir() throws IOException{
        String[] currentDirSplit = currentDir.getCanonicalPath().split("/");
        formattedCurDir = "";
        final String rootAccount = account.getRootDirName().substring(0, 
                account.getRootDirName().length()-1);
        
        boolean isLocatedName = false;
        
        for (String curDir : currentDirSplit) {
            if (curDir.equals(rootAccount)) isLocatedName = true;
            if (isLocatedName) formattedCurDir +=("/"+curDir);
        }
    }
    
    public File getCurrentDir() {
        return currentDir;
    }
    
    public String getCurrentDirPath() throws IOException{
        return currentDir.getCanonicalPath();
    }
    
    public File[] listFiles(){
        return currentDir.listFiles();
    }
    
    public File[] getOnlyFiles(){
        return listFiles() == null ? null : 
                (listFiles().length > 0 ? currentDir.listFiles(f -> !f.isDirectory()) : null);
    }
    
    public File[] getOrderedFiles(){
        File[] files = getOnlyFiles();
        if (files == null) return null;
        
        Arrays.sort(files, (File o1, File o2) -> o1.getName().compareTo(o2.getName()));
        return files;
    }
    
    public File[] getOnlyDirectories(){
        return listFiles() == null ? null : 
                (listFiles().length > 0 ? currentDir.listFiles(File::isDirectory) : null);
    }
    
    public File[] getOrderedDirs(){
        File[] dirs = getOnlyDirectories();
        if(dirs == null) return null;
        
        Arrays.sort(dirs, (File o1, File o2) -> o1.getName().compareTo(o2.getName()));
        return dirs;
    }
    
    public void setCurrentDir(File currentDir) {
        this.currentDir = currentDir;
    }
    
}
