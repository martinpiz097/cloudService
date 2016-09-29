/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.model.packages;

import java.io.File;
import java.io.Serializable;
import org.martin.cloudCommon.system.Account;

/**
 *
 * @author martin
 */
public class UpdatePackage implements Serializable{
    private final File currentDir;
    private final Account account;

    public UpdatePackage(File currentDir, Account account) {
        this.currentDir = currentDir;
        this.account = account;
    }

    public File getCurrentDir() {
        return currentDir;
    }

    public Account getAccount() {
        return account;
    }
    
}
