/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.interfaces;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author martin
 */
public interface BinSerializable {
    
    public void writeBytesFrom(String path) throws IOException;
    
    public void writeBytesFrom(File origin) throws IOException;
    
    public void writeBytesTo(String path) throws IOException;
    
    public void writeBytesTo(File destiny) throws IOException;

}
