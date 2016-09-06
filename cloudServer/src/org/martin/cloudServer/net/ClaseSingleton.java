/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudServer.net;

/**
 *
 * @author martin
 */
public class ClaseSingleton {
    
    private ClaseSingleton() {
    }
    
    public static ClaseSingleton getInstance() {
        return SIngletonHolder.INSTANCE;
    }
    
    private static class SIngletonHolder {

        private static final ClaseSingleton INSTANCE = new ClaseSingleton();
    }
}
