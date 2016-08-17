/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.model;

import java.io.Serializable;
import org.martin.cloudCommon.system.Encryptor;

/**
 *
 * @author martin
 */
public class DefaultUser implements Serializable{
    
    private String nick;
    private int[] password;
    
    public DefaultUser(String nick, String password) {
        this.nick = nick;
        this.password = Encryptor.getEncryptedText(password);
    }

    public String getNick() {
        return nick;
    }

    public String getPassword() {
        return Encryptor.getDecryptedText(password);
    }
    
    public void setPassword(String password){
        this.password = Encryptor.getEncryptedText(password);
    }
    
}