/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.system;

import java.io.File;
import org.martin.cloudCommon.model.User;

/**
 *
 * @author martin
 */
public class SysManager {
//    public static String getRootDirectoryName(User user){
//        return SysInfo.ROOT_FOLDER_NAME + "/" + 
//                user.getId() + user.getNick() + "/";
//    }
    
    public static String getSystemRootDir(){
        return SysInfo.ROOT_FOLDER_NAME;
    }
    
    public static File getRootDir(User user){
        return new File(SysInfo.ROOT_FOLDER_NAME, getRootDirectoryName(user));
    }
    
    public static File getRootDir(String rootUserDir){
        return new File(SysInfo.ROOT_FOLDER_NAME, rootUserDir);
    }
    
    public static String getRootDirectoryName(User user){
        return user.getId() + user.getNick() + "/";
    }
    
    public static String getCurrentTimeStamp(){
        return null;
    }
}
