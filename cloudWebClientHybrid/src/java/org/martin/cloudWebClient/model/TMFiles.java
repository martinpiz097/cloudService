/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudWebClient.model;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.martin.cloudCommon.system.Converter;
import org.martin.cloudCommon.system.Utilities;

/**
 *
 * @author martin
 */
public class TMFiles implements WebTableModel{

    private final LinkedList<File> files;

    public TMFiles(LinkedList<File> files) {
        this.files = files;
    }
    
    public TMFiles(File[] files){
        this.files = new LinkedList<>(Arrays.asList(files));
    }
    
    @Override
    public int getRowCount() {
        return files.size();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int columnIndex) {
        switch(columnIndex){
            case 0: return "Nombre";
            case 1: return "Tamaño";
            case 2: return "Última modificación";
            default: return "Opciones";
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return Object.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        File f = files.get(rowIndex);
        
        switch(columnIndex){
            case 0: return f.getName();
            case 1: return Converter.getConvertedSize(f.length());
            case 2: return Utilities.getDateToString(new Date(f.lastModified()));
            default:{
                try {
                    return "<a class = \"opFile\" href=\"deleteFile.do?file="+f.getCanonicalPath()+"\">"+
                            "<button class='btnOPFile'type='button'>Eliminar</button>"+
                            "</a>"+
                            "<a class = \"opFile\" href=\"renameFile.jsp?file="+f.getCanonicalPath()+"\">"+
                            "<button class='btnOPFile'type='button'>Renombrar</button>"+
                            "</a>"+
                            "<a class = \"opFile\" href=\"downloadFile.do?file="+f.getCanonicalPath()+"\">"+
                            "<button class='btnOPFile'type='button'>Descargar</button>"+
                            "</a>";
                } catch (IOException ex) {
                    Logger.getLogger(TMFiles.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }
    
}
