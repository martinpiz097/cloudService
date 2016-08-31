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
public class User implements Serializable{

    private final long id;
    private String nick;
    private int[] password;
    
    public User(long id, String nick, String password){
        this.id = id;
        this.nick = nick;
        if (password == null) 
            this.password = null;
        else
            this.password = Encryptor.getEncryptedText(password);
    }
    
    public User(String nick, String password) {
        this.id = -1;
        this.nick = nick;
        if (password == null) 
            this.password = null;
        else
            this.password = Encryptor.getEncryptedText(password);
    }

    public boolean isNull(){
        return nick == null && password == null;
    }

    public long getId() {
        return id;
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