/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudClient.gui.model;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import org.martin.cloudClient.gui.GUICloudClient;
import org.martin.cloudCommon.model.packages.UpdatePackage;
import org.martin.cloudCommon.system.Command;
import org.martin.cloudCommon.system.Utilities;

/**
 *
 * @author martin
 */
public class TMFiles implements TableModel{
    
    private File[] files;
    private static final NumberFormat nf = new DecimalFormat("#0.0");
    private static final short BYTE_IN_KILOBYTE = 1024;
    
    public TMFiles(File[] files) {
        this.files = files;
        if (this.files != null) {
            if (this.files.length > 0) {
                Arrays.sort(files, (File o1, File o2) -> o1.getName().toLowerCase()
                        .compareTo(o2.getName().toLowerCase()));
            }
        }
        else
            this.files = new File[0];
        
    }

    public TMFiles(File directory) {
        this(directory.listFiles(f -> !f.isDirectory()));
    }

    public File getSelectedFile(int index){
        return files[index];
    }
    
    public void setFiles(File[] files){
        this.files = files;
    }
    
    private long raise(int number, int numberOfTimes){

        for (int i = 1; i < numberOfTimes; i++) 
            number *= number;

        return number;
    }
    
    public void setFiles(File directory){
        this.files = directory.listFiles(f -> !f.isDirectory());
    }

    @Override
    public int getRowCount() {
        return files.length;
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch(columnIndex){
            case 0: return "Nombre";
            case 1: return "Tamaño";
            default: return "Última modificación";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Considerar edicion de celdas para cambiar nombres
        return columnIndex == 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        final File f = files[rowIndex]; 
        final long tamaño = f.length();
        final Date d = new Date(f.lastModified());
        
        switch(columnIndex){
            case 0: return f.getName();
            
            case 1:{
                if (tamaño < raise(BYTE_IN_KILOBYTE, 2))
                    return nf.format((double) tamaño / 1000) + "kB";
                
                else if (tamaño < raise(BYTE_IN_KILOBYTE, 3))
                    return nf.format((double) tamaño / raise(BYTE_IN_KILOBYTE, 2)) + "MB";
                
                else
                    return nf.format((double) tamaño / raise(BYTE_IN_KILOBYTE, 3)) + "GB";
            }
            default: return Utilities.getDateToString(d);
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            try {
                final String newName = aValue.toString();
                final String pathCurFile = getSelectedFile(rowIndex).getCanonicalPath();
                if (newName.isEmpty()) return;
  
                if (newName.equals(getValueAt(rowIndex, columnIndex))) return;
                
                final Command cmdRename = new Command("@rename");
                cmdRename.addOption(pathCurFile);
                cmdRename.addOption(newName);
                GUICloudClient.getInstance().connector.sendCommand(cmdRename);
                GUICloudClient.getInstance().connector.sendUpdateRequest(
                        GUICloudClient.getInstance().cliPackage.getCurrentDirPath(), 
                        GUICloudClient.getInstance().cliPackage.getUserNick());

                GUICloudClient.getInstance().cliPackage
                        .update((UpdatePackage) GUICloudClient.getInstance()
                                .connector.getReceivedObject());
                
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(TMFiles.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
    }

}
