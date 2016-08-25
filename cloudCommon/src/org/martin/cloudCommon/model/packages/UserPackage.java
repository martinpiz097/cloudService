/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.model.packages;

import java.io.Serializable;

/**
 *
 * @author martin
 */
public class UserPackage implements Serializable{

    private final ClientPackage cliPackage;

    public UserPackage(ClientPackage cliPackage) {
        this.cliPackage = cliPackage;
    }
    
    public boolean hasClientPackage(){
        return cliPackage != null;
    }

    public ClientPackage getCliPackage() {
        return cliPackage;
    }
    
}