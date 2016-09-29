/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebaserializacionxml;

import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

/**
 *
 * @author martin
 */
public class PruebaSerializacionXML {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException, IOException, JAXBException {
        Archivo a = new Archivo("archivo1");

        byte[] b;
        for (int i = 0; i < 100; i++) {
            b = new byte[1000];
            for (int j = 0; j < 127; j++)
                b[i] = (byte) i;
            a.addByte(b);
        }
     
        JAXBContext context = JAXBContext.newInstance(Archivo.class);
        Marshaller marshaller = context.createMarshaller();
        
        FileOutputStream fos = new FileOutputStream("archivo1.xml");
        
        marshaller.marshal(a, fos);
        fos.close();
    }    
}
