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
public class Encryptor {
    
    private static final byte MULTIPLIER_FACTOR = 2;
    private static final byte ADDER_NUMBER = 3;
    
    public static int[] getEncryptedText(String text){
        final int lenText = text.length();
        int[] textCode = new int[lenText];
        for (int i = 0; i < lenText; i++) 
            textCode[i] = ((int)text.charAt(i))*MULTIPLIER_FACTOR+ADDER_NUMBER;
        
        return textCode;
    }
    
    public static String getDecryptedText(int[] textCode){
        final int lenArray = textCode.length;
        String text = "";
        for (int i = 0; i < lenArray; i++)
            text+=(char)((textCode[i]-ADDER_NUMBER)/MULTIPLIER_FACTOR);
        
        return text;
    }
}
