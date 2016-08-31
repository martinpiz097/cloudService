///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.martin.cloudServer.db;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.util.LinkedList;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.martin.cloudCommon.model.DefaultUser;
//import org.martin.cloudCommon.model.User;
//import org.martin.cloudCommon.system.SysInfo;
///**
// *
// * @author martin
// */
//public final class DbManagerOld {
//    
//    private final File fileUsers;
//    private LinkedList<User> users;
//    private transient ObjectOutputStream writer;
//    private transient ObjectInputStream reader;
//
//    public DbManagerOld() throws IOException {
//        fileUsers = new File(SysInfo.ROOT_FOLDER_NAME + "/" + SysInfo.DB_USERS_FILE_NAME);
//        users = new LinkedList<>();
//        if(!fileUsers.exists()) fileUsers.createNewFile();
//        loadScript();
//        printUsers();
//        // Los fileChannel de java io pueden trabajar con archivos más rapido
//        // ya que el so puede optimizarlos
////        FileChannel fcInput = new FileInputStream(fileUsers).getChannel();
////        FileChannel fcOutput = new FileOutputStream(fileUsers).getChannel();
//    }
//
//    public File getFileUsers() {
//        return fileUsers;
//    }
//    
//    public boolean isValidRegistration(DefaultUser newUser){
//        return !users.stream().anyMatch((u) -> u.getNick().equalsIgnoreCase(newUser.getNick()));
//    }
//
//    public boolean isValidRegistration(String nick){
//        return !users.stream()
//                .anyMatch(u -> u.getNick().equals(nick));
//    }
//    
//    public boolean isValidUser(String nick, String passw){
//        return users.stream().anyMatch((u) -> u.getNick()
//                .equalsIgnoreCase(nick) && u.getPassword().equalsIgnoreCase(passw));
//    }
//    
//    public void addUser(User user){
//        users.add(user);
//        updateScript();
//    }
//    
//    public void addUser(String nick, String passw){
//        users.add(new User(users.size()+1, nick, passw));
//        updateScript();
//    }
//    
//    public void addUser(DefaultUser user) throws IOException{
//        users.add(new User(users.size()+1, user.getNick(), user.getPassword()));
//        updateScript();
//    }
//
//    public int getUsersCount(){
//        return users.size();
//    }
//
//    public User getUser(int id){
//        return users.stream()
//                .filter(u -> u.getId() == id).findFirst().orElse(null);
//    }
//    
//    public User getUser(String nick){
//        return users.stream()
//                .filter(u -> u.getNick().equalsIgnoreCase(nick)).findFirst().orElse(null);
//    }
//    
//    public User getUser(String nick, String password){
//        return users.stream()
//                .filter(u -> u.getNick().equalsIgnoreCase(nick) && u.getPassword()
//                        .equalsIgnoreCase(password)).findFirst().orElse(null);
//    }        
//    public LinkedList<User> getUsers(){
//        return users;
//    }
//    
//    private void printUsers(){
//        System.out.println("Usuarios\n--------\n");
//        System.out.println("Cantidad de usuarios: " + users.size());
//        users.forEach(System.out::println);
//    }
//    
//    public void removeUser(int id){
//        users.removeIf(u -> u.getId() == id);
//        updateScript();
//    }
//    
//    public void removeUser(String nick){
//        users.removeIf((u) -> u.getNick().equalsIgnoreCase(nick));
//        updateScript();
//    }
//    
//    public void removeAllUsers(){
//        users.clear();
//        updateScript();
//    }
//    
//    public void updateScript(){
//        try {
//            writer = new ObjectOutputStream(new FileOutputStream(fileUsers));
//            writer.writeObject(users);
//            writer.flush();
//            writer.close();
//        } catch (IOException ex) {
//            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    
//    public void loadScript(){
//        try {
//            reader = new ObjectInputStream(new FileInputStream(fileUsers));
//            final Object obj = reader.readObject();
//
//            if (obj == null) updateScript();
//            else if (obj instanceof LinkedList) users = (LinkedList<User>) obj;
//            else updateScript();
//            
//        } catch (IOException ex) {
//            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (ClassNotFoundException ex) {
//            Logger.getLogger(DbManager.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//    
//}