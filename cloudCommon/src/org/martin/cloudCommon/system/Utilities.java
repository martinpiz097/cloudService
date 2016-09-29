/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.system;

import java.sql.Time;
import java.util.Date;

/**
 *
 * @author martin
 */
public class Utilities {
    
    private static final byte ZERO = 0;
    private static final char SLASH = '/';
    private static final char DOUBLE_POINT = ':';
    private static final short INITIAL_TIME = 1900;
    
    public static String getDateToString(Date d){
        
        String day = String.valueOf(d.getDate());
        String month = String.valueOf(d.getMonth());
        String year = String.valueOf(d.getYear() + INITIAL_TIME);
        String hours = String.valueOf(d.getHours());
        String minutes = String.valueOf(d.getMinutes());
        String seconds = String.valueOf(d.getSeconds());
        
        if (d.getDate() < 10) day = ZERO + day;
        if (d.getMonth() < 10) month = ZERO + month;
        if (d.getHours() < 10) hours = ZERO + hours;
        if (d.getMinutes() < 10) minutes = ZERO + minutes;
        if (d.getSeconds() < 10) seconds = ZERO + seconds;
        
        return day+SLASH+month+SLASH+year+"  "+
                hours+DOUBLE_POINT+minutes+DOUBLE_POINT+seconds;
    }
    
}
