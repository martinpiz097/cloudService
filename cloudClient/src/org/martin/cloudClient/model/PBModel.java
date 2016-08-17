/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudClient.model;

import javax.swing.BoundedRangeModel;
import javax.swing.JProgressBar;
import javax.swing.event.ChangeListener;
import org.martin.cloudCommon.system.CloudInfo;

/**
 *
 * @author martin
 */
public class PBModel implements BoundedRangeModel{

    // Los espacios serán mostrados en megas
    //private final JProgressBar progressBar;
    private final CloudInfo info;
    private int currentValue;
    
    public PBModel(CloudInfo info) {
        this.info = info;
        this.currentValue = (int) (info.getUsedSpace()/1024/1024);
    }
    
    @Override
    public int getMinimum() {
        return 0;
    }

    @Override
    public void setMinimum(int newMinimum) {
    }

    @Override
    public int getMaximum() {
        return (int) (info.getTotalSpace() / 1024 / 1024);
    }

    @Override
    public void setMaximum(int newMaximum) {
        info.setTotalSpace(newMaximum);
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
    }

    @Override
    public void setExtent(int newExtent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setRangeProperties(int value, int extent, int min, int max, boolean adjusting) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void addChangeListener(ChangeListener x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeChangeListener(ChangeListener x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
