/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.system;

import java.io.File;
import java.io.IOException;
import org.martin.cloudCommon.interfaces.BinSerializable;

/**
 *
 * @author martin
 */
public class Serializer implements BinSerializable{

    private static final Serializer serializer;

    static{
        serializer = new Serializer();
    }
    
    public static Serializer getInstance(){
        return serializer;
    }
    
    @Override
    public void writeBytesFrom(String path) throws IOException {
        
    }

    @Override
    public void writeBytesFrom(File origin) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeBytesTo(String path) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void writeBytesTo(File destiny) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
