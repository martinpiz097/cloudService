/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudClient.gui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import org.martin.cloudClient.net.Connector;
import org.martin.cloudCommon.model.packages.ClientPackage;
import org.martin.cloudCommon.system.Command;

/**
 *
 * @author martin
 */
public class Grapher {

    private static final Grapher g = new Grapher();
    // Si no funciona probar con new Grapher.getClass();
    private static final ImageIcon fileImg = new ImageIcon(
            g.getClass().getResource("/org/martin/cloudClient/gui/icons/file 48x48"));
    
    public static void graphFiles(File[] files, JPanel panel, Connector con){
        final ClientPackage cp = GUICloudClient.getInstance().getCliPackage();
        
        JLabel lbl;
        for (File file : files) {
            lbl = new JLabel();
            lbl.setHorizontalTextPosition(JLabel.CENTER);
            lbl.setVerticalTextPosition(JLabel.BOTTOM);
            lbl.setIcon(fileImg);
            lbl.setBorder(new EtchedBorder(Color.BLUE, Color.WHITE));
            lbl.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(java.awt.event.MouseEvent evt){
                    if (evt.getClickCount() == 2) {
                        final Command cmd = new Command("@access", file.getPath());
                        try {
                            con.sendCommand(cmd);
                            GUICloudClient.getInstance().getCliPackage().setCurrentDir((File) con.getReceivedObject());
                            GUICloudClient.getInstance().updateAll();
                        } catch (IOException ex) {
                            Logger.getLogger(Grapher.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(Grapher.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            lbl.setVisible(true);
            panel.add(lbl);
        }
        panel.updateUI();
    }
}
