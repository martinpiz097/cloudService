
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.xml.parsers.ParserConfigurationException;
import org.jespxml.JespXML;
import org.jespxml.excepciones.TagHijoNotFoundException;
import org.jespxml.modelo.Tag;
import org.martin.cloudCommon.system.Archive;
import org.xml.sax.SAXException;

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
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, TagHijoNotFoundException, URISyntaxException {
        JespXML analizer = new JespXML(new URL("http", "localhost", 8086, "/testWS/index.jsp"));
        Tag root = analizer.leerXML();
        System.out.println("ROOT: "+root.getNombre());
        List<Tag> tags = root.getTagHijoByName("file", Tag.Cantidad.TODOS_LOS_TAGS);
//        Casa c;
//        
//        Queue<Casa> casas = new LinkedList<>();
//    
//        for (Tag tag : tags) {
//            casas.add(new Casa(Integer.parseInt(tag.getTagHijoByName("id").getContenido()), 
//                    tag.getTagHijoByName("ciudad").getContenido()));
//        }
//        
//        System.out.println("Casas\n-----");
//        casas.stream().forEach(System.out::println);
        
        Tag name = tags.get(0).getTagHijoByName("name");
        Tag bytes = tags.get(0).getTagHijoByName("bytes");
        Archive archive = new Archive("/home/martin/"+name.getContenido());
        archive.readFromXML(bytes.getContenido());
        archive.create();
        
        Archive index = new Archive(new URI("http://localhost:8086/testWS/index.jsp"));
        index.read();
        File destiny = new File("/home/martin/index.jsp");
        index.
        
    }
    
}
