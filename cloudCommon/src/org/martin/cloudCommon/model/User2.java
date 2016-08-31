///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.martin.cloudCommon.model;
//
//import java.io.Serializable;
//import org.martin.cloudCommon.system.Cloud;
//import org.martin.cloudCommon.system.SysInfo;
//
///**
// *
// * @author martin
// */
//public class User2 extends DefaultUser implements Serializable{
//    
//    private final int id;
//    private final Cloud cloud;
//
//    public User() {
//        super(null, null);
//        id = 0;
//        cloud = null;
//    }
//
//    public User(int id, String nick, String password) {
//        super(nick, password);
//        this.id = id;
//        this.cloud = new Cloud(this);
//    }
//
//    public User(int id, Cloud cloud, String nick, String password) {
//        super(nick, password);
//        this.id = id;
//        this.cloud = cloud;
//    }
//    
//    @Override
//    public boolean isNull(){
//        return id == 0 && cloud == null && super.isNull();
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public Cloud getCloud() {
//        return cloud;
//    }
//
//    @Override
//    public String toString() {
//        return "User{" + "id=" + id + ", cloud=" + cloud + '}';
//    }
//
//}
