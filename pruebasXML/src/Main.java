
import java.beans.Expression;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author martin
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ClassNotFoundException, IOException {
        File file = new File("object.dat");
        long ti;
        if(!file.exists()) file.createNewFile();

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
        
        ti = start();
        for (int i = 0; i < 100; i++)
            oos.writeObject(new Casa(i+1, "direccion"+(i+1)));
        System.out.println("WriteObject: "+finish(ti));
        
        oos.close();
        
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
        
        Object obj;
        ti = start();
        for(;;){
            try{
                ois.readObject();
            }catch(IOException ex){
                break;
            }
        }
        System.out.println("ReadObject: "+finish(ti));
        ois.close();
    }
    
    public static long start(){
        return System.currentTimeMillis();
    }
    
    public static long finish(long ti){
        return System.currentTimeMillis()-ti;
    }
}
