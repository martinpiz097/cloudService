/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.model.packages;

import java.io.Serializable;
import org.martin.cloudCommon.model.DefaultUser;

/**
 *
 * @author martin
 */
public class UserPackage implements Serializable{
    private final DefaultUser user;
    private final boolean isLogin; 

    public UserPackage(DefaultUser user, boolean isLogin) {
        this.user = user;
        this.isLogin = isLogin;
    }

    public DefaultUser getUser(){
        return user;
    }
    
    public String getNick(){
        return user.getNick();
    }
    
    public String getPassword(){
        return user.getPassword();
    }
    
    public boolean isLogin() {
        return isLogin;
    }
    
}
