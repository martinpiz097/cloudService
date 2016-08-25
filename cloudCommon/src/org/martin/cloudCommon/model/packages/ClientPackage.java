/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.model.packages;

import java.io.File;
import java.io.Serializable;
import org.martin.cloudCommon.model.DefaultUser;
import org.martin.cloudCommon.system.CloudInfo;

/**
 *
 * @author martin
 */
public class ClientPackage implements Serializable{
    private final DefaultUser df;
    private CloudInfo info;
    private File currentDir;
    
    public static ClientPackage newInstance(DefaultUser df, CloudInfo info, File currentDir){
        return new ClientPackage(df, info, currentDir);
    }
    
    public ClientPackage(DefaultUser df, CloudInfo info, File currentDir) {
        this.df = df;
        this.info = info;
        this.currentDir = currentDir;
    }

    public DefaultUser getDf() {
        return df;
    }

    public String getUserNick(){
        return df.getNick();
    }
    
    public String getUserPassword(){
        return df.getPassword();
    }
    
    public CloudInfo getInfo() {
        return info;
    }

    public void setInfo(CloudInfo info) {
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
