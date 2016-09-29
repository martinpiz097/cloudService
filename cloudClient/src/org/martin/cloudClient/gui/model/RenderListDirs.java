/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudClient.gui.model;

import java.awt.Color;
import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.border.EtchedBorder;

/**
 *
 * @author martin
 */
public class RenderListDirs extends JLabel implements ListCellRenderer<String>{

    private final ImageIcon img;

    public RenderListDirs() {
        img = new ImageIcon(getClass().getResource("/org/martin/cloudClient/gui/icons/folder 32x32.png"));
    }
    
    @Override
    public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel lbl = new JLabel(value);
        lbl.setIcon(img);
        lbl.setOpaque(true);
        
        if (isSelected) 
            lbl.setBackground(Color.CYAN);
        
        else
            lbl.setBackground(Color.WHITE);
        
        lbl.setVisible(true);
        
        
        return lbl;
    }
    
}
