///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package org.martin.cloudWebClient.servlets;
//
//import java.io.File;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Iterator;
//import java.util.List;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.swing.JOptionPane;
//import org.apache.tomcat.util.http.fileupload.FileItem;
//import org.apache.tomcat.util.http.fileupload.FileItemFactory;
//import org.apache.tomcat.util.http.fileupload.FileUploadException;
//import org.apache.tomcat.util.http.fileupload.RequestContext;
//import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
//import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
//
///**
// *
// * @author martin
// */
//@WebServlet(name = "FileUploadServlet", urlPatterns = {"/uploadFile.do"})
//public class FileUploadServlet extends HttpServlet {
//
//    /**
//     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
//     * methods.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        response.setContentType("text/html;charset=UTF-8");
//        try (PrintWriter out = response.getWriter()) {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet FileUploadServlet</title>");            
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet FileUploadServlet at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        }
//    }
//
//    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
//    /**
//     * Handles the HTTP <code>GET</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        doPost(request, response);
//    }
//
//    /**
//     * Handles the HTTP <code>POST</code> method.
//     *
//     * @param request servlet request
//     * @param response servlet response
//     * @throws ServletException if a servlet-specific error occurs
//     * @throws IOException if an I/O error occurs
//     */
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//      
//        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
//   if (isMultipart) {
//       FileItemFactory factory = new DiskFileItemFactory();
//       ServletFileUpload upload = new ServletFileUpload(factory);
//       try {
//            List items = upload.parseRequest((RequestContext) request);
//            Iterator iterator = items.iterator();
//            while (iterator.hasNext()) {
//                 FileItem item = (FileItem) iterator.next();
//                    if (!item.isFormField()) {
//                        String fileName = item.getName();
//                        String root = getServletContext().getRealPath("/");
//                        File path = new File(root + "/../../web/files");
//                        // Si la carpeta raiz en donde los archivos seran subidos
//                        // no existe se crea en conjunto a sus carpetas padre
//                        if (!path.exists()) {
//                            boolean status = path.mkdirs();
//                        }
//                        File uploadedFile = new File(path + "/" + fileName);
//                        item.write(uploadedFile);
//                        BeanArchivo archivo = new BeanArchivo();
//                        archivo.setNombre(fileName);
//                        archivo.setRuta(uploadedFile.getAbsolutePath());
//                        daoarchivo.insert(archivo);
//                        }
//                    }
//       } catch (FileUploadException e) {
//          e.printStackTrace();
//          } catch (Exception e) {
//          e.printStackTrace();
//          }
//      }
//    }
//
//    /**
//     * Returns a short description of the servlet.
//     *
//     * @return a String containing servlet description
//     */
//    @Override
//    public String getServletInfo() {
//        return "Short description";
//    }// </editor-fold>
//
//}
