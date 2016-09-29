/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author martin
 */
public class Casa {
    private final int id;
    private final String ciudad;

    public Casa(int id, String ciudad) {
        this.id = id;
        this.ciudad = ciudad;
    }

    public int getId() {
        return id;
    }

    public String getCiudad() {
        return ciudad;
    }

    @Override
    public String toString() {
        return "Casa{" + "id=" + id + ", ciudad=" + ciudad + '}';
    }

}
