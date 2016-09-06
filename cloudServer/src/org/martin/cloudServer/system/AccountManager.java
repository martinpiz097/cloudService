/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudServer.system;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.martin.cloudCommon.model.User;
import org.martin.cloudCommon.system.Account;
import org.martin.cloudCommon.system.Archive;
import org.martin.cloudCommon.system.Folder;
import static org.martin.cloudCommon.system.SysManager.*;
import org.martin.cloudServer.db.DbManager;

/**
 *
 * @author martin
 */

// Antiguo Cloud
public class AccountManager implements Serializable{
    
    // Cada accountManager tendra un objeto dbManager para darle
    // la carga de concurrencia al motor de Base de Datos
    private final transient DbManager dbManager;
    private final Account account;
    private final File rootDirectory;
    private transient FileInputStream reader;
    private transient FileOutputStream writer;
    
    public AccountManager(Account account) throws SQLException {
        this(account.getRootDirName(), account);
    }

    public AccountManager(String rootDirectoryPath, Account account) throws SQLException {
        this.account = account;
        dbManager = new DbManager();
        this.rootDirectory = getRootDir(rootDirectoryPath);
        if (!rootDirectory.exists()) rootDirectory.mkdir();
    }

    public static void createRootDirectory(User user){
        File userDir = getRootDir(user);
        userDir.mkdir();
    }
    
    public DbManager getDbManager(){
        return dbManager;
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
     * Evalua si hay espacio suficiente para poder insertar más archivos
     * @param fileLenght tamaño del archivo que se desea insertar
     * @return true si el espacio disponible permite incluir más archivos; falso
     * en caso contrario
     * @throws java.sql.SQLException En caso de existir problemas al hacer consultas
     * a la base de datos
     */
    
    public boolean hasAvailableSpace(long fileLenght) throws SQLException{
        return dbManager.hasAvailableSpace(fileLenght, account.getId());
    }

    /**
     * Crea en la ruta especificada en el objeto Archive un nuevo archivo
     * subido desde el equipo cliente
     * @param archive Objeto con los datos del archivo nuevo, incluyendo su contenido en
     * bytes
     * @throws IOException en caso de existir problemas al escribir o leer en un archivo
     */
    
    public void uploadFile(Archive archive) throws IOException, SQLException{
        uploadFile(archive.getParent(), archive);
    }
    
    /**
     * Crea en la ruta especificada en el objeto Archive un nuevo archivo
     * subido desde el equipo cliente
     * @param archive Objeto con los datos del archivo nuevo, incluyendo su contenido en
     * bytes
     * @throws IOException en caso de existir problemas al escribir o leer en un archivo
     */
    
    public void uplF(Archive archive) throws IOException, SQLException{
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

    public void uploadFile(String parentFolder, Archive archive) throws IOException, SQLException{
        archive.create();
        addFile(archive.length());
        System.out.println("Archivo subido!");
        System.out.println("Ruta del nuevo archivo remoto: " + archive.getCanonicalPath());
    }

    /**
     * Crea en la ruta especificada en los datos de ambos parámetros un nuevo archivo
     * subido desde el equipo cliente
     * @throws IOException en caso de existir problemas al escribir o leer en un archivo
     * @param parentFolder carpeta remota en donde se alojará el nuevo archivo
     * @param archive Objeto con los datos del archivo nuevo, incluyendo su contenido en
     * bytes
     * @throws java.sql.SQLException
     */
    
    public void uplF(String parentFolder, Archive archive) throws IOException, SQLException{
        System.out.println("Entro al uplF");
        uploadFile(parentFolder, archive);
    }
    
    // Javadoc pendiente
    
    public void uploadFile(String remotePath, LinkedList<byte[]> bytes) throws IOException{
        File f = new File(remotePath);
        f.createNewFile();
        writer = new FileOutputStream(f);
        
        bytes.stream().forEach(b -> {
            try {
                writer.write(b, 0, b.length);
            } catch (IOException ex) {
                Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        writer.close();
    }
    
    public void uplF(String remotePath, LinkedList<byte[]> bytes) throws IOException{
        uploadFile(remotePath, bytes);
    }
    
    public void uploadFile(String remoteFolderPath, String name, LinkedList<byte[]> bytes) throws IOException{
        final boolean endWithSlash = remoteFolderPath.endsWith("/");
        if (endWithSlash)
            uploadFile(remoteFolderPath+name, bytes);
        
        else
            uploadFile(remoteFolderPath+"/"+name, bytes);
    }
    
    public void uplF(String remoteFolderPath, String name, LinkedList<byte[]> bytes) throws IOException{
        uploadFile(remoteFolderPath, name, bytes);
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
        toDownload.writeBytesFrom(new File(remotePath));
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
    
    public void copyFile(String currentPath, String newFolder) throws IOException, SQLException{
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
        addFile(newFile.length());
    }
    
    /**
     * Copia un archivo remoto en otra ubicación(puede ser igual que la actual)
     * @param currentPath Ubicación actual del archivo
     * @param newFolder nueva ubicación del archivo
     * @throws IOException en caso de existir problemas al copiar
     */
    
    public void cpF(String currentPath, String newFolder) throws IOException, SQLException{
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
    public void deleteFile(String path) throws SQLException{
        final File toDelete = getFile(path);
        if(!toDelete.exists()) return;
        
        // remvoeFile --> llama al procedimiento de la base de datos
        // para cambiar el espacio actual en uso
        removeFile(toDelete.length());
        toDelete.delete();
    }
    
    /**
     * Elimina un archivo indicando su ubicación, si el archivo no existe pasa nada
     * @param path Ubicación del archivo a eliminar
     */
    public void delF(String path) throws SQLException{
        deleteFile(path);
    }

    /**
     * Elimina una carpeta y indicando su ubicación, si la carpeta no existe pasa nada.
     * Si la carpeta no está vacía se borra todo su contenido
     * @param path Ubicación de la carpeta a eliminar
     * @throws java.io.IOException En caso de existir problemas al borrar
     */
    public void deleteFolder(String path) throws IOException{
        final File folder = new File(path);
        if(!folder.exists()) return;
        
        if(folder.listFiles() == null) return;
        
        if(folder.listFiles().length == 0) return;
        
        for (File file : folder.listFiles())
            if (file.isDirectory()) deleteFolder(file.getCanonicalPath());
            else file.delete();

        folder.delete();
    }

    /**
     * Elimina una carpeta y indicando su ubicación, si la carpeta no existe pasa nada.
     * Si la carpeta no está vacía se borra todo su contenido
     * @param path Ubicación de la carpeta a eliminar
     * @throws java.io.IOException En caso de existir problemas al borrar
     */
    public void delD(String path) throws IOException{
        deleteFolder(path);
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
        File localFld;
        if (folder.hasFolders())
            for (Folder fld : folder.getFolders())
                localFld = new File(fld.getCanonicalPath());
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
    /**
     * Baja una carpeta con todo su contenido desde la nube (Método recursivo)
     * @param localPath Ruta del equipo cliente en donde se almacenará la carpeta
     * @param remotePath Ruta del servidor en donde se aloja la carpeta solicitada
     * @return Objeto Folder con la información solicitada
     * @throws IOException en caso de existir problemas al descargar la carpeta
     */
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
                    a.addBytes(buff);
                reader.close();
                result.addArchive(a);
            }
        }
        
        return result;
        
    }
    
    /**
     * Baja una carpeta con todo su contenido desde la nube (Método recursivo)
     * @param localPath Ruta del equipo cliente en donde se almacenará la carpeta
     * @param remotePath Ruta del servidor en donde se aloja la carpeta solicitada
     * @return Objeto Folder con la información solicitada
     * @throws IOException en caso de existir problemas al descargar la carpeta
     */
    public Folder dwnD(String localPath, String remotePath) throws IOException{
        return downloadFolder(localPath, remotePath);
    }
    
    // El path tanto para el copyFolder y cutFolder representa la ruta COMPLETA
    // de la carpeta, no solo su nombre
    
    public void copyFolder(String path, String newParent) throws IOException{
        File toCopy = goToFolder(path);
        File newFld;
        
        if (toCopy.getParent().equalsIgnoreCase(newParent)){
            int cont = 0;
            String pt = path;
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
                    Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                try {
                    copyFile(f.getCanonicalPath(), newFld.getCanonicalPath());
                } catch (IOException | SQLException ex) {
                    Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    
    public void cpD(String path, String newParent) throws IOException{
        copyFolder(path, newParent);
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
                    Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else{
                try {
                    cutFile(f.getCanonicalPath(), newFld.getCanonicalPath());
                } catch (IOException ex) {
                    Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        toCut.delete();
    }
    
    // ../ Comando para regresar a carpeta anterior
    
    /**
     * Obtiene un archivo de acuerdo a la ruta espeficada
     * @param path Ruta del archivo a acceder
     * @return Objeto File solicitado
     */
    
    public File getFile(String path){
        File f = new File(path);
        return f.exists() ? f : null;
    }

    /**
     * Obtiene un archivo de acuerdo a la ruta espeficada
     * @param path Ruta del archivo a acceder
     * @return Objeto File solicitado
     */
    public File access(String path){
        return getFile(path);
    }
    
    public File getFile(String parent, String name){
        File f = new File(parent, name);
        return f.exists() ? f : null;
    }

    
    /**
     * Regresa a la carpeta anterior a la actual(si la carpeta actual es la raiz
     * no se puede retroceder)
     * @param currentFolder Carpeta actual de trabajo
     * @return Objeto File con los datos de la carpeta padre de la actual
     * @throws IOException en caso de problemas de acceso a la carpeta
     */
    public File goBack(String currentFolder) throws IOException{
        System.out.println("goBack;Directorio raiz: "+rootDirectory.getCanonicalPath());
        if (currentFolder.equalsIgnoreCase(rootDirectory.getCanonicalPath()))
            return rootDirectory;
        else
            return goToFolder(currentFolder).getParentFile();
    }
    
    /**
     * Regresa a la carpeta anterior a la actual(si la carpeta actual es la raiz
     * no se puede retroceder)
     * @param currentFolder Carpeta actual de trabajo
     * @return Objeto File con los datos de la carpeta padre de la actual
     * @throws IOException en caso de problemas de acceso a la carpeta
     */
    
    public File back(String currentFolder) throws IOException{
        return goBack(currentFolder);
    }

    public File goToFolder(File currentFolder, String folderName){
        final File[] files = currentFolder.listFiles();
        return Arrays.stream(files)
                .filter(f -> f.isDirectory() && f.getName().equalsIgnoreCase(folderName))
                .findFirst().get();
    }
    
    public File goToFolder(String path){
        if (path.equalsIgnoreCase(rootDirectory.getParent())) 
            return rootDirectory;
        else{
            System.out.println("goToFolder;Ruta de la carpeta a acceder: "+
                        new File(path).getParent());
            return new File(path);
        }
    
    }
    
    /**
     * Crea una carpeta en la ruta especificada
     * @param folderName Nombre de la carpeta a crear
     * @param path Ruta en donde se alojará la nueva carpeta
     */
    public void mkdir(String folderName, String path){
        try {
            File f = new File(path, folderName);
            System.out.println("Path de la nueva carpeta: " + f.getCanonicalPath());
            if (!f.exists()) {
                f.mkdir();
                System.out.println("Se creop la carpeta");
            }
        } catch (IOException ex) {
            Logger.getLogger(AccountManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Crea una carpeta en la ruta especificada
     * @param folderName Nombre de la carpeta a crear
     * @param path Ruta en donde se alojará la nueva carpeta
     */
    public void mkd(String folderName, String path){
        mkdir(folderName, path);
    }
    
    public File getRootDirectory() {
        return rootDirectory;
    }

    public String getRootDirectoryAsString(){
        return "/";
    }
    
    public long getUsedSpace() {
        return account.getUsedSpace();
    }
    
    public void addFile(long fileLenght) throws SQLException{
        account.addUsedSpace(fileLenght);
        dbManager.addFile(fileLenght, account.getId());
    }
    
    public void removeFile(long fileLenght) throws SQLException{
        account.removeUsedSpace(fileLenght);
        dbManager.removeFile(fileLenght, account.getId());
    }

    public long getAvailableSpace(){
        return getTotalSpace()-getUsedSpace();
    }
    
    public long getTotalSpace() {
        return account.getTotalSpace();
    }

    public Date getCreationDate() {
        return account.getCreationDate();
    }

    @Override
    public String toString() {
        return "AccountManager{" + "rootDirectory=" + rootDirectory + ", account=" + account + '}';
    }

}
