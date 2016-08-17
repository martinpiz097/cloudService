/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudCommon.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.martin.cloudCommon.model.User;

/**
 *
 * @author martin
 */
public class Cloud implements Serializable{
    
    private final File rootDirectory;
    // Todos los espacios son en bytes
    private long usedSpace;
    private long totalSpace;
    private final Date creationDate;
    private static transient Calendar c;
    private transient FileInputStream reader;
    private transient FileOutputStream writer;
    
    public Cloud(User ownerUser) {
        rootDirectory = new File(SysInfo.ROOT_FOLDER_NAME + "/" + 
                ownerUser.getId() + ownerUser.getNick() + "/");
        if(!rootDirectory.exists()) rootDirectory.mkdir();
        usedSpace = 0;
        totalSpace = SysInfo.TOTAL_SPACE;
        c = new GregorianCalendar();
        creationDate = c.getTime();
    }

    // El goBack se puede hacer desde el cliente guardando la ruta actual
    // o las listas ocupadas y ademas se puede ofrecer la opcion de actualizar

    /**
     * Devuelve un booleano si existe o no el archivo según la ruta especificada
     * @param path Ruta a analizar
     * @return true si el archivo existe; false en caso contrario
     */
    
    public boolean fileExists(String path){
        return new File(path).exists();
    }

    public boolean validateNewFile(String newFolder, String nameFile){
        return !Arrays.stream(getFiles(newFolder))
                .anyMatch(f -> f.getName().equalsIgnoreCase(nameFile));
    }
    
    /**
     * Crea en la ruta especificada en el objeto Archive un nuevo archivo
     * subido desde el equipo cliente
     * @param newArchive Objeto con los datos del archivo nuevo, incluyendo su contenido en
     * bytes
     * @throws IOException en caso de existir problemas al escribir o leer en un archivo
     */
    
    public void uploadFile(Archive newArchive) throws IOException{
        uploadFile(newArchive.getParent(), newArchive);
    }
    
    /**
     * Crea en la ruta especificada en el objeto Archive un nuevo archivo
     * subido desde el equipo cliente
     * @param archive Objeto con los datos del archivo nuevo, incluyendo su contenido en
     * bytes
     * @throws IOException en caso de existir problemas al escribir o leer en un archivo
     */
    
    public void uplF(Archive archive) throws IOException{
        uploadFile(archive.getParent(), archive);
    }
    
    /**
     * Crea en la ruta especificada en los datos de ambos parámetros un nuevo archivo
     * subido desde el equipo cliente
     * @throws IOException en caso de existir problemas al escribir o leer en un archivo
     * @param parentFolder carpeta remota en donde se alojará el nuevo archivo
     * @param archive Objeto con los datos del archivo nuevo, incluyendo su contenido en
     * bytes
     */

    public void uploadFile(String parentFolder, Archive archive) throws IOException{
        File file = new File(parentFolder, archive.getName());
        file.createNewFile();
        writer = new FileOutputStream(file);
        archive.getQueueBytes().stream().forEach((b) -> {
            try {
                writer.write(b, 0, b.length);
            } catch (IOException ex) {
                Logger.getLogger(Cloud.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        writer.close();
    }

    /**
     * Crea en la ruta especificada en los datos de ambos parámetros un nuevo archivo
     * subido desde el equipo cliente
     * @throws IOException en caso de existir problemas al escribir o leer en un archivo
     * @param parentFolder carpeta remota en donde se alojará el nuevo archivo
     * @param archive Objeto con los datos del archivo nuevo, incluyendo su contenido en
     * bytes
     */
    
    public void uplF(String parentFolder, Archive archive) throws IOException{
        uploadFile(parentFolder, archive);
    }
    
    /**
     * Entrega un objeto de tipo Archive con los datos del archivo que será descargado
     * incluyendo la ruta local que se le asignó por parámetros
     * @param localPath ruta local en donde se almacenará el archivo en el 
     * equipo cliente
     * @param remotePath ruta remota en donde el archivo se aloja en el servidor
     * @return Objeto Archive con los datos del archivo a descargar
     * @throws IOException en caso de existir problemas al escribir o leer en un archivo
     */
    
    public Archive downloadFile(String localPath, String remotePath) throws IOException{
        Archive toDownload = new Archive(localPath);
        reader = new FileInputStream(new File(remotePath));
        byte[] buff = new byte[1024];
        while (reader.read(buff, 0, buff.length) != -1)            
            toDownload.addByte(buff);
        
        reader.close();
        return toDownload;
    }
    
    /**
     * Entrega un objeto de tipo Archive con los datos del archivo que será descargado
     * incluyendo la ruta local que se le asignó por parámetros
     * @param localPath ruta local en donde se almacenará el archivo en el 
     * equipo cliente
     * @param remotePath ruta remota en donde el archivo se aloja en el servidor
     * @return Objeto Archive con los datos del archivo a descargar
     * @throws IOException en caso de existir problemas al escribir o leer en un archivo
     */
    
    public Archive dwnF(String localPath, String remotePath) throws IOException{
        return downloadFile(localPath, remotePath);
    }

    /**
     * Copia un archivo remoto en otra ubicación(puede ser igual que la actual)
     * @param currentPath Ubicación actual del archivo
     * @param newFolder nueva ubicación del archivo
     * @throws IOException en caso de existir problemas al copiar
     */
    
    public void copyFile(String currentPath, String newFolder) throws IOException{
        File f = getFile(currentPath);
        if(f == null) return;
        reader = new FileInputStream(f);
        File newFile;

        // Si la ruta nueva es igual a la actual
        if (f.getParent().equalsIgnoreCase(newFolder)) {
            int cont = 0;
            String cp = currentPath;
            // Se elimina el / en caso de tenerlo para poder comprobar
            // si existe un archivo con esta ruta pero añadiendo un número
            if (cp.endsWith("/")) cp = cp.substring(0, cp.length()-1);
            
            // Se hace un bucle hasta que no existan archivos con el nombre
            // solicitado
            while (getFile(cp + cont) != null)                
                cont++;
            
            // Se crea el archivo con el nombre generado con el contador
            newFile = new File(f.getParent(), f.getName()+cont);
        }
        else
            newFile = new File(newFolder, f.getName());
        
        newFile.createNewFile();
        writer = new FileOutputStream(newFile);
        byte[] buff = new byte[1024];
        while (reader.read(buff, 0, buff.length) != -1) {
            writer.write(buff, 0, buff.length);
            writer.flush();
        }
        writer.close();
        reader.close();
    }
    
    /**
     * Copia un archivo remoto en otra ubicación(puede ser igual que la actual)
     * @param currentPath Ubicación actual del archivo
     * @param newFolder nueva ubicación del archivo
     * @throws IOException en caso de existir problemas al copiar
     */
    
    public void cpF(String currentPath, String newFolder) throws IOException{
        copyFile(currentPath, newFolder);
    }
    
    /**
     * Mueve un archivo remoto en otra ubicación (no debe ser igual que la actual)
     * @param currentPath Ubicación actual del archivo
     * @param newFolder nueva ubicación del archivo
     * @throws IOException en caso de existir problemas al copiar
     */
    
    public void cutFile(String currentPath, String newFolder) throws IOException{
        File f = getFile(currentPath);
        if(f == null || f.getParent().equalsIgnoreCase(newFolder)) return;

        reader = new FileInputStream(f);
        File newFile = new File(newFolder, f.getName());
        newFile.createNewFile();
        writer = new FileOutputStream(newFile);

        byte[] buff = new byte[1024];
        while (reader.read(buff, 0, buff.length) != -1) {
            writer.write(buff, 0, buff.length);
            writer.flush();
        }
        writer.close();
        reader.close();
        f.delete();
    }
    
     /**
     * Mueve un archivo remoto en otra ubicación (no debe ser igual que la actual)
     * @param currentPath Ubicación actual del archivo
     * @param newFolder nueva ubicación del archivo
     * @throws IOException en caso de existir problemas al copiar
     */
    
    public void ctF(String currentPath, String newFolder) throws IOException{
        cutFile(currentPath, newFolder);
    }

    /**
     * Elimina un archivo indicando su ubicación, si el archivo no existe pasa nada
     * @param path Ubicación del archivo a eliminar
     */
    public void deleteFile(String path){
        final File toDelete = getFile(path);
        if (toDelete != null) toDelete.delete();
    }
    
    /**
     * Elimina un archivo indicando su ubicación, si el archivo no existe pasa nada
     * @param path Ubicación del archivo a eliminar
     */
    public void delF(String path){
        deleteFile(path);
    }
        
    /**
     * Devuelve un objeto File con los datos de la carpeta raiz de la nube
     * @return Objeto File con los datos de la carpeta raiz de la nube
     */
    // se retorna un objeto nuevo ya que asi se permite actualizar en caso de que
    // se hayan editado algunos datos
    public File goToRoot(){
        return rootDirectory;
    }
    
    /**
     * Devuelve un objeto File con los datos de la carpeta raiz de la nube
     * @return Objeto File con los datos de la carpeta raiz de la nube
     */
    public File root(){
        return goToRoot();
    }

    /**
     *  Devuelve un array con los archivos existentes según la ubicación entregada
     * por parámetros
     * @param path Ubicación de la carpeta solicitada
     * @return Array de tipo File[] con los archivos y carpetas del directorio solicitado
     */
    public File[] getFiles(String path){
        return goToFolder(path).listFiles();
    }
    
    /**
     * Devuelve un array con los archivos existentes según la ubicación entregada
     * por parámetros
     * @param path Ubicación de la carpeta solicitada
     * @return Array de tipo File[] con los archivos y carpetas del directorio solicitado
     */
    public File[] list(String path){
        return goToFolder(path).listFiles();
    }
    
    /**
     * Devuelve una lista con los archivos existentes según la ubicación entregada
     * por parámetros
     * @param path Ubicación de la carpeta solicitada
     * @return Lista con los archivos y carpetas del directorio solicitado
     */
    
    public LinkedList<File> getFilesAsList(String path){
        return new LinkedList<>(Arrays.asList(getFiles(path)));
    }
    
    /**
     * Devuelve una lista con los archivos existentes según la ubicación entregada
     * por parámetros
     * @param path Ubicación de la carpeta solicitada
     * @return Lista con los archivos y carpetas del directorio solicitado
     */
    public LinkedList<File> listAsList(String path){
        return new LinkedList<>(Arrays.asList(getFile(path)));
    }

    /**
     * Sube una carpeta a la nube
     * @param folder Carpeta a subir representada como un objeto Folder
     * @throws IOException en caso de existir problemas al subir cada uno de sus archivos
     */
    public void uploadFolder(Folder folder) throws IOException{
        for (Archive archive : folder.getAll())
            uploadFile(archive);
    }
    
    /**
     * Sube una carpeta a la nube
     * @param folder Carpeta a subir representada como un objeto Folder
     * @throws IOException en caso de existir problemas al subir cada uno de sus archivos
     */
    public void uplD(Folder folder) throws IOException{
        uploadFolder(folder);
    }
    
    // Este metodo tambien sirve para obtener una carpeta con todo su contenido,
    // no necesariamente se descargará
    public Folder downloadFolder(String localPath, String remotePath) throws IOException{
        final File toDownload = new File(remotePath);
        Folder result = new Folder(localPath);
        Archive a;
        byte[] buff;
        
        // Revisar el caso de tener carpetas con nombres iguales en el cliente
        for (File f : toDownload.listFiles()) {
            if (f.isDirectory())
                result.addFolder(downloadFolder(localPath+"/"+f.getName(), 
                        remotePath+"/"+f.getName()));
            else{
                a = new Archive(f.getCanonicalPath());
                reader = new FileInputStream(f);
                buff = new byte[1024];
                while (reader.read(buff, 0, buff.length) != -1)                    
                    a.addByte(buff);
                reader.close();
                result.addArchive(a);
            }
        }
        
        return result;
        
    }
    
    // El path tanto para el copyFolder y cutFolder representa la ruta COMPLETA
    // de la carpeta, no solo su nombre
    
    public void copyFolder(String path, String newParent) throws IOException{
        File toCopy = goToFolder(path);
        File newFld;
        
        if (toCopy.getParent().equalsIgnoreCase(newParent)){
            int cont = 0;
            String pt = path;
            if(pt.endsWith("/")) pt = pt.substring(0, pt.length());
            String newName = pt;
            
            while (goToFolder(pt+cont).exists()) cont++;

            newName += cont;
            newFld = new File(path, newName);
            newFld.mkdir();
        }
        else{
            newFld = new File(newParent, toCopy.getName());
            newFld.mkdir();
        }

        Arrays.stream(getFiles(toCopy.getCanonicalPath())).forEach((f) -> {
            if (f.isDirectory()) {
                try {
                    copyFolder(f.getCanonicalPath(), newFld.getCanonicalPath());
                } catch (IOException ex) {
                    Logger.getLogger(Cloud.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                try {
                    copyFile(f.getCanonicalPath(), newFld.getCanonicalPath());
                } catch (IOException ex) {
                    Logger.getLogger(Cloud.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    public void cutFolder(String path, String newParent) throws IOException{
        File toCut = goToFolder(path);
        File newFld;
        if(toCut.getParent().equalsIgnoreCase(newParent)) return;
        
        newFld = new File(newParent, toCut.getName());
        newFld.mkdir();

        Arrays.stream(getFiles(toCut.getCanonicalPath())).forEach((f) -> {
            if (f.isDirectory()) {
                try {
                    cutFolder(f.getCanonicalPath(), newFld.getCanonicalPath());
                } catch (IOException ex) {
                    Logger.getLogger(Cloud.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                try {
                    cutFile(f.getCanonicalPath(), newFld.getCanonicalPath());
                } catch (IOException ex) {
                    Logger.getLogger(Cloud.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        toCut.delete();
    }
    
    public void deleteFolder(String path){
        goToFolder(path).delete();
    }
    
    // ../ Comando para regresar a carpeta anterior
    public File goToFolder(File currentFolder, String folderName){
        if (currentFolder.getAbsolutePath().equalsIgnoreCase(rootDirectory.getAbsolutePath()) && 
                folderName.equalsIgnoreCase("../")) 
                return currentFolder;
        
        else if (folderName.equalsIgnoreCase("../")) 
            // Se retorna un nuevo objeto file ya que la informacion del objeto
            // currentFolder puede estar desactualizada
            return new File(currentFolder.getParent());
        
        else
            return Arrays.stream(getFiles(currentFolder.getAbsolutePath()))
                        .filter((f) -> f.getName().equalsIgnoreCase(folderName) && f.isDirectory())
                        .findFirst().orElse(currentFolder);
    }
    
    public File goToFolder(String path){
        if (path.equalsIgnoreCase(rootDirectory.getParent())) 
            return rootDirectory;
        else
            return new File(path);
    }
    
    public File getFile(String path){
        File f = new File(path);
        return f.exists() ? f : null;
    }
    
    public File getFile(String parent, String name){
        File f = new File(parent, name);
        return f.exists() ? f : null;
    }

    
    public File goBack(String currentFolder) throws IOException{
        if (currentFolder.equalsIgnoreCase(rootDirectory.getCanonicalPath()))
            return rootDirectory;
        else
            return goToFolder(currentFolder).getParentFile();
    }
    
    public boolean isNewFilePermitted(long fileLenght){
        return fileLenght <= getAvailableSpace();
    }
    
    public File getRootDirectory() {
        return rootDirectory;
    }

    public double getUsedSpace() {
        return usedSpace;
    }
    
    public void addFile(long fileLenght){
        usedSpace+=fileLenght;
    }

    public double getAvailableSpace(){
        return totalSpace-usedSpace;
    }
    
    public double getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(long totalSpace) {
        this.totalSpace = totalSpace;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public static Calendar getC() {
        return c;
    }
    
}
