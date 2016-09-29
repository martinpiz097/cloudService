/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebaserializacionxml;

import java.io.Serializable;
import java.util.LinkedList;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author martin
 */
@XmlRootElement
public class Archivo implements Serializable{
    
    private String nombre;
    private LinkedList<byte[]> bytes;

    public Archivo() {
        nombre = null;
        bytes = new LinkedList<>();
    }
    
    public Archivo(String nombre) {
        this.nombre = nombre;
        this.bytes = bytes = new LinkedList<>();
    }
    
    public void addByte(byte[] b){
        bytes.add(b);
    }

    public String getNombre() {
        return nombre;
    }

    public LinkedList<byte[]> getBytes() {
        return bytes;
    }

    @Override
    public String toString() {
        return "Archivo{" + "nombre=" + nombre + ", bytes=" + bytes + '}';
    }
    
}
