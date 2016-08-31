
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import org.martin.cloudCommon.model.User;
import org.martin.cloudCommon.model.packages.UserPackage;

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
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Socket cliente = new Socket("localhost", 2000);
        ObjectOutputStream oos = new ObjectOutputStream(cliente.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(cliente.getInputStream());
        oos.writeObject(new UserPackage(new User("user3", "pass3"), false));
        oos.flush();
        Object obj = ois.readObject();
        while (obj == null) 
            obj = ois.readObject();
        
        if (obj instanceof Boolean)
            System.out.println(obj);
    }
    
}
