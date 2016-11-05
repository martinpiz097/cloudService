/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudWebClient.model;


/**
 *
 * @author martin
 */
public interface WebTableModel {
    public int getRowCount();

    public int getColumnCount();
    
    public String getColumnName(int columnIndex);
    
    public Class<?> getColumnClass(int columnIndex);

    public Object getValueAt(int rowIndex, int columnIndex);

    public void setValueAt(Object aValue, int rowIndex, int columnIndex);
}
