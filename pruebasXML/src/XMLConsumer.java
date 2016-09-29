
import java.io.File;
import java.net.URI;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author martin
 */
public class XMLConsumer extends File{

    
    
    public XMLConsumer(String pathname) {
        super(pathname);
    }

    public XMLConsumer(String parent, String child) {
        super(parent, child);
    }

    public XMLConsumer(File parent, String child) {
        super(parent, child);
    }

    public XMLConsumer(URI uri) {
        super(uri);
    }
   
}
