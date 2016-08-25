/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.system;

/**
 *
 * @author martin
 */
public class SysInfo {
    public static final String DEFAULT_HOST = "190.161.190.114";
    public static final String LOCALHOST = "localhost";
    public static final String ROOT_FOLDER_NAME = "electroCloud";
    public static final String OS_NAME = System.getProperty("os.name");
    public static final String DB_USERS_FILE_NAME = "dbCloud.db";
    public static final long TOTAL_SPACE = 1073741824;
    public static final int DEFAULT_PORT = 2000;
    public static final long SVU_COMMAND = 110L;
    public static final long SVU_CLOUD_INFO = 111L;
    public static final long SVU_DEFAULT_USER = 210L;
    public static final long SVU_CLOUD = 211L;
    public static final long SVU_USER = 310L;
    public static final long SVU_USER_PACKAGE = 311L;
    
    public enum TypeSw{
        SERVER, CLIENT;
    }
    
}
