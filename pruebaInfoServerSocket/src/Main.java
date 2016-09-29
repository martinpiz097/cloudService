
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

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
     */
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(2000);
        new Thread(()->{
            try {
                Socket s = new Socket("0.0.0.0", 2000);
                System.out.println("Cliente Hostname: "+s.getInetAddress().getHostName());
                System.out.println("Cliente HostAddress: "+s.getInetAddress().getHostAddress());
                System.out.println("Cliente CanonicalHostName: "+s.getInetAddress().getCanonicalHostName());
                System.out.println("Cliente LocalPort: "+s.getLocalPort());
                System.out.println("Cliente LocalSocketAddress: "+s.getLocalSocketAddress().toString());
                System.out.println("Cliente Conectado: "+s.isConnected());
            } catch (IOException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
        Socket cliente = ss.accept();
        System.out.println("ServerCliente Hostname: "+cliente.getInetAddress().getHostName());
        System.out.println("ServerCliente HostAddress: "+cliente.getInetAddress().getHostAddress());
        System.out.println("ServerCliente CanonicalHostName: "+cliente.getInetAddress().getCanonicalHostName());
        System.out.println("ServerCliente LocalPort: "+cliente.getLocalPort());
        System.out.println("ServerCliente LocalSocketAddress: "+cliente.getLocalSocketAddress().toString());
        
        System.out.println("Server Hostname: "+ss.getInetAddress().getHostName());
        System.out.println("Server HostAddress: "+ss.getInetAddress().getHostAddress());
        System.out.println("Server CanonicalHostName: "+ss.getInetAddress().getCanonicalHostName());
        System.out.println("Server LocalPort: "+ss.getLocalPort());
        System.out.println("Server LocalSocketAddress: "+ss.getLocalSocketAddress().toString());
        ss.close();
    }
    
}
