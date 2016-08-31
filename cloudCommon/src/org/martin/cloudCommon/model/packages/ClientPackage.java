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
    private final User df;
    private Account info;
    private File currentDir;

    public static ClientPackage newInstance(User df, Account info, File currentDir){
        return new ClientPackage(df, info, currentDir);
    }
    
    public ClientPackage(User df, Account info, File currentDir) {
        this.df = df;
        this.info = info;
        this.currentDir = currentDir;
    }

    public User getDf() {
        return df;
    }

    public String getUserNick(){
        return df.getNick();
    }
    
    public String getUserPassword(){
        return df.getPassword();
    }
    
    public Account getInfo() {
        return info;
    }

    public void setInfo(Account info) {
        this.info = info;
    }

    public File getCurrentDir() {
        return currentDir;
    }

    public File[] listFiles(){
        return currentDir.listFiles();
    }
    
    public File[] getOnlyFiles(){
        return currentDir.listFiles(f -> !f.isDirectory());
    }
    
    public File[] getOnlyDirectories(){
        return currentDir.listFiles(File::isDirectory);
    }
    
    public void setCurrentDir(File currentDir) {
        this.currentDir = currentDir;
    }
    
}
