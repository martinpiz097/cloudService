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
import javax.swing.JOptionPane;
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

    private final ImageIcon fileImg;

    private static Grapher g;
    
    public static Grapher getGrapher(){
        if(g == null) g = new Grapher();
        return g;
    }

    public Grapher() {
        fileImg = new ImageIcon(getClass()
                .getResource("/org/martin/cloudClient/gui/icons/file 48x48.png"));
    }
    
    public void graphFiles(File[] files, JPanel panel){
        panel.removeAll();
        JLabel lbl;
        
        if (files != null) {
            for (File file : files) {
                lbl = new JLabel(file.getName());
                lbl.setForeground(Color.BLACK);
                lbl.setHorizontalTextPosition(JLabel.CENTER);
                lbl.setVerticalTextPosition(JLabel.BOTTOM);
                lbl.setIcon(fileImg);
                lbl.setVisible(true);
                panel.add(lbl);
            }
            panel.updateUI();
        }
    }
}
