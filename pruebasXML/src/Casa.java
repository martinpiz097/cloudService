
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author martin
 */
public class Casa implements Serializable{
    private int id;
    private String direccion; 

    public Casa() {
    }

    public Casa(int id, String direccion) {
        this.id = id;
        this.direccion = direccion;
    }

    public int getId() {
        return id;
    }

    public String getDireccion() {
        return direccion;
    }

    @Override
    public String toString() {
        return "Casa{" + "id=" + id + ", direccion=" + direccion + '}';
    }
//
//    @Override
//    public void writeExternal(ObjectOutput out) throws IOException {
//        out.writeInt(id);
//        out.writeUTF(direccion);
//    }
//
//    @Override
//    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
//        this.id = in.readInt();
//        this.direccion = in.readUTF();
//    }
}
