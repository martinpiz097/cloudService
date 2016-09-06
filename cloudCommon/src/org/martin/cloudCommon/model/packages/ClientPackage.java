/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.model.packages;

import java.io.File;
import java.io.Serializable;
import org.martin.cloudCommon.model.User;
import org.martin.cloudCommon.system.Account;
import org.martin.cloudCommon.system.SysInfo;

/**
 *
 * @author martin
 */
public class ClientPackage implements Serializable{
    private final Account account;
    private File currentDir;

    public static ClientPackage newInstance(Account account, File currentDir){
        return new ClientPackage(account);
    }
    
    public ClientPackage(Account account) {
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
    
    public Account getAccount() {
        return account;
    }

    public File getCurrentDir() {
        return currentDir;
    }
    
    public File[] listFiles(){
        return currentDir.listFiles();
    }
    
    public File[] getOnlyFiles(){
        return listFiles() == null ? null : currentDir.listFiles(f -> !f.isDirectory());
    }
    
    public File[] getOnlyDirectories(){
        return listFiles() == null ? null : currentDir.listFiles(File::isDirectory);
    }
    
    public void setCurrentDir(File currentDir) {
        this.currentDir = currentDir;
    }
    
}
