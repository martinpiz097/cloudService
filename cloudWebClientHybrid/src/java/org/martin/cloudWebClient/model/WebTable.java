/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudWebClient.model;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.jsp.JspWriter;

/**
 *
 * @author martin
 * @param <T>
 */
public class WebTable<T> {
    private final JspWriter out;
    private final String id;
    private final String clazz;
    private final byte border;
    private final LinkedList<T> elements;
    private WebTableModel model;
    
    public WebTable(JspWriter out, String id, String clazz, byte border) {
        this.out = out;
        this.id = id;
        this.clazz = clazz;
        this.border = border;
        elements = new LinkedList<>();
    }

    public WebTable(JspWriter out, String id, String clazz, byte border, LinkedList<T> elements) {
        this.out = out;
        this.id = id;
        this.clazz = clazz;
        this.border = border;
        this.elements = elements;
    }
    
    public WebTable(JspWriter out, String id, String clazz, byte border, T[] elements) {
        this.out = out;
        this.id = id;
        this.clazz = clazz;
        this.border = border;
        this.elements = new LinkedList<>(Arrays.asList(elements));
    }

    public void addElement(T element){
        elements.add(element);
    }
    
    private void print(Object obj){
        try {
            out.print(obj);
        } catch (IOException ex) {
            Logger.getLogger(WebTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void println(Object obj){
        try {
            out.println(obj);
        } catch (IOException ex) {
            Logger.getLogger(WebTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void printTH(Object obj){
        println("<th>"+obj+"</th>");
    }
    
    private void printTD(Object obj){
        println("<td>"+obj+"</td>");
    }
    
    private void startTR(){
        println("<tr>");
    }
    
    private void endTR(){
        println("</tr>");
    }
    
    public void draw() throws UnknownModelException{
        if(model == null) throw new UnknownModelException();
        
        else{
            print("<table");
            if (id != null)
                print(" id="+id);
            if (!clazz.isEmpty())
                print(" class="+clazz);
                
            if (border > 0)
                print(" border="+border);

            print(">");

            startTR();
            for (int i = 0; i < model.getColumnCount(); i++)
                printTH(model.getColumnName(i));
            endTR();
            
            for (int i = 0; i < model.getRowCount(); i++) {
                startTR();
                for (int j = 0; j < model.getColumnCount(); j++)
                    printTD(model.getValueAt(i, j));
                endTR();
            }
            
            println("</table>");
        }
    }
    
    public void setModel(WebTableModel model){
        this.model = model;
    }
}
