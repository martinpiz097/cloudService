/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudClient.gui.model;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 *
 * @author martin
 */
public class TCRFiles implements TableCellRenderer{

    private final File[] files;
    private int foundFileIndex;
    private final ImageIcon icon;
    private Color background;

    public TCRFiles(){
        this.files = null;
        foundFileIndex = -1;
        icon = new ImageIcon(getClass()
                .getResource("/org/martin/cloudClient/gui/icons/file 48x48.png"));
    }
    
    public TCRFiles(File[] files) {
        this.files = files;
        foundFileIndex = -1;
        icon = new ImageIcon(getClass()
                .getResource("/org/martin/cloudClient/gui/icons/file 48x48.png"));
    }

    public void paintFoundFile(int index){
        foundFileIndex = index;
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        final JLabel lbl = new JLabel();
        
        if (column == 0)
            lbl.setIcon(icon);
    
        lbl.setText(value.toString());

        if (isSelected) lbl.setBackground(Color.CYAN);
        else if(row == foundFileIndex) lbl.setBackground(Color.RED);
        
        lbl.setOpaque(true);
        return lbl;
    }
    
}
