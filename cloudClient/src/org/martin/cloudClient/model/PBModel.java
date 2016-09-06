/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudClient.model;

import javax.swing.BoundedRangeModel;
import javax.swing.event.ChangeListener;
import org.martin.cloudCommon.system.Account;

/**
 *
 * @author martin
 */
public class PBModel implements BoundedRangeModel{

    // Los espacios serán mostrados en megas
    //private final JProgressBar progressBar;
    private final Account acoount;
    private int currentValue;
    private int minimun;
    private int maximun;
    
    public PBModel(Account account) {
        this.acoount = account;
        this.currentValue = (int) (account.getUsedSpace()/1024/1024);
        this.minimun = 0;
        this.maximun = (int) (account.getTotalSpace()/1024/1024);
    }
    
    @Override
    public int getMinimum() {
        return minimun;
    }

    @Override
    public void setMinimum(int newMinimum) {
        this.minimun = newMinimum;
    }

    @Override
    public int getMaximum() {
        return maximun;
    }

    @Override
    public void setMaximum(int newMaximum) {
        acoount.setTotalSpace(newMaximum*1024*1024);
        maximun = (int) (acoount.getTotalSpace()/1024/1024);
    }

    @Override
    public int getValue() {
        return currentValue;
    }

    @Override
    public void setValue(int newValue) {
        currentValue = newValue;
    }

    @Override
    public void setValueIsAdjusting(boolean b) {
    }

    @Override
    public boolean getValueIsAdjusting() {
        return true;
    }

    @Override
    public int getExtent() {
        return getMaximum()-getMinimum()+1;
    }

    @Override
    public void setExtent(int newExtent) {
    }

    @Override
    public void setRangeProperties(int value, int extent, int min, int max, boolean adjusting) {
    }

    @Override
    public void addChangeListener(ChangeListener x) {
    }

    @Override
    public void removeChangeListener(ChangeListener x) {
    }
    
}
