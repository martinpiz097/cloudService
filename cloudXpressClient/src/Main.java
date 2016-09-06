
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.martin.cloudCommon.model.packages.UserPackage;
import org.martin.cloudCommon.system.Command;

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
     * @throws java.io.IOException
     * @throws java.lang.ClassNotFoundException
     */
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        
        Socket cliente;
        ObjectOutputStream oos;
        ObjectInputStream ois;
//        for (int i = 0; i < 20; i++) {
//            cliente = new Socket("localhost", 2000);
//            oos = new ObjectOutputStream(cliente.getOutputStream());
//            ois = new ObjectInputStream(cliente.getInputStream());
//            //oos.writeObject(new UserPackage(new User("user3", "pass3"), false));
//            oos.writeObject(Command.newRegU("nick"+(i+1)+"", "pass"+(i+1)+""));
//            oos.flush();
//            Object obj = ois.readObject();
//            while (obj == null) 
//                obj = ois.readObject();
//        
//            if (obj instanceof Boolean)
//                System.out.println(obj);
//        }
        
        
        for (int i = 0; i < 20; i++) {
            cliente = new Socket("localhost", 2000);
            oos = new ObjectOutputStream(cliente.getOutputStream());
            ois = new ObjectInputStream(cliente.getInputStream());
            //oos.writeObject(new UserPackage(new User("user3", "pass3"), false));
            oos.writeObject(Command.newLoginU("nick"+(i+1)+"", "pass"+(i+1)+""));
            Object obj = ois.readObject();
            while (obj == null)
                obj = ois.readObject();
        
            if (obj instanceof UserPackage){
                System.out.println("Tiene paquete cliente: "+((UserPackage) obj).hasClientPackage());
                
                oos = new ObjectOutputStream(cliente.getOutputStream());
                ois = new ObjectInputStream(cliente.getInputStream());
                oos.writeObject(Command.root);
                
                
                Object obj2 = ois.readObject();
                while (obj2 == null)
                    obj2 = ois.readObject();
            
                System.out.println(((File)obj2).getCanonicalPath());
                oos.writeObject(new Command("@close"));
                oos.close();
                ois.close();
                cliente.close();
            }
        }    
    }
}