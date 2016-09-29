/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.system;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JOptionPane;

/**
 *
 * @author martin
 */
public class Converter {
    public static final short KILOBYTES_IN_BYTES = 1024;
    public static final int MEGABYTES_IN_BYTES = 1024*1024;
    public static final int GIGABYTES_IN_BYTES = 1024*1024*1024;
    public static final NumberFormat numFormat = new DecimalFormat("#0.00");
    
    public enum MODE{
        BYTE_INTO_KILOBYTE, BYTE_INTO_MEGABYTE,
        BYTE_INTO_GIGABYTE     
    }
    
    public static double convert(long bytes, MODE mode){
        if(mode == null) return bytes;
        
        switch(mode){
            case BYTE_INTO_GIGABYTE: return (double)bytes / (GIGABYTES_IN_BYTES);
            case BYTE_INTO_MEGABYTE: return (double)bytes / (MEGABYTES_IN_BYTES);
            case BYTE_INTO_KILOBYTE: return (double)bytes / KILOBYTES_IN_BYTES;
            default: return bytes;
        }
    }
    
    public static String getFormattedDecimal(double value){
        return numFormat.format(value);
    }

    private static long raise(int number, int numberOfTimes){

        for (int i = 1; i < numberOfTimes; i++) 
            number *= number;

        return number;
    }
    
    public static String getConvertedSize(long size){
        
        if (size / GIGABYTES_IN_BYTES >= 1)
            return getFormattedDecimal(convert(size, MODE.BYTE_INTO_GIGABYTE)) + " GB";
        else if (size / MEGABYTES_IN_BYTES >= 1)
            return getFormattedDecimal(convert(size, MODE.BYTE_INTO_MEGABYTE)) + "MB";
        else if (size / KILOBYTES_IN_BYTES >= 1)
            return getFormattedDecimal(convert(size, MODE.BYTE_INTO_KILOBYTE)) + " KB";
        else
            return getFormattedDecimal(convert(size, null)) + " B";
    
    
    }

}
