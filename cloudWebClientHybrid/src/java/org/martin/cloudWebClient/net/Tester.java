
package org.martin.cloudWebClient.net;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.martin.cloudCommon.system.SysInfo;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author martin
 */
public class Tester {
    public static boolean isConnected(){
        try {
            return new Socket(SysInfo.LOCALHOST, SysInfo.DEFAULT_PORT).isConnected();
        } catch (IOException ex) {
            return false;
        }
    }
    
    
}
