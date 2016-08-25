/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.model;

import java.io.Serializable;
import org.martin.cloudCommon.system.Cloud;

/**
 *
 * @author martin
 */
public class User extends DefaultUser implements Serializable{
    
    private final int id;
    private final Cloud cloud;

    public User() {
        super(null, null);
        id = 0;
        cloud = null;
    }

    public User(int id, String nick, String password) {
        super(nick, password);
        this.id = id;
        this.cloud = new Cloud(this);
    }
    
    @Override
    public boolean isNull(){
        return id == 0 && cloud == null && super.isNull();
    }

    public int getId() {
        return id;
    }

    public Cloud getCloud() {
        return cloud;
    }

    @Override
    public String toString() {
        return id+".- "+super.getNick()+"-"+super.getPassword();
    }
    
}
