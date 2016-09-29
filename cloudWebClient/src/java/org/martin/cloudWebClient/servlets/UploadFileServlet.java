/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudWebClient.servlets;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.martin.cloudCommon.model.packages.ClientPackage;
import org.martin.cloudCommon.model.packages.TransferPackage;
import org.martin.cloudCommon.system.Archive;
import org.martin.cloudCommon.system.Command;
import org.martin.cloudCommon.system.SysInfo;
import org.martin.cloudWebClient.net.Connector;

/**
 *
 * @author martin
 */
@WebServlet(name = "UploadFileServlet", urlPatterns = {"/uploadFile.do"})
public class UploadFileServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        
        if (session.getAttribute("cliPackage") == null || 
                session.getAttribute("connector") == null) {
            response.sendRedirect("index.jsp");
            return;
        }
        
        ClientPackage cp = (ClientPackage) session.getAttribute("cliPackage");
        Connector con = (Connector) session.getAttribute("connector");
        
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setSizeThreshold(1024);
        factory.setRepository(new File(SysInfo.TEMP_FOLDER_NAME));

        ServletFileUpload upload = new ServletFileUpload(factory);

        try {
            List<FileItem> partes = upload.parseRequest(request);
            Archive toUpload;
            Command cmdUpload;
            File file;
            
            for (FileItem item : partes) {
                if (!item.isFormField()) {
                    file = new File(SysInfo.TEMP_FOLDER_NAME, item.getName());
                    item.write(file);
                    toUpload = new Archive(cp.getCurrentDir(), item.getName());
                    toUpload.writeBytesFrom(file);
                    cmdUpload = new Command(Command.uplF.getOrder(),
                            file.getCanonicalPath(), cp.getCurrentDirPath());
                    con.sendTransferPackage(new TransferPackage(cmdUpload, toUpload));
                    file.delete();
                }
            }

            con.sendUpdateRequest(cp.getCurrentDirPath(), cp.getUserNick());
            cp.update(con.getUpdatesReceived());

            session.setAttribute("cliPackage", cp);
            response.sendRedirect("management.jsp");
        } catch (FileUploadException ex) {
            JOptionPane.showMessageDialog(null, "FileUploadException");
            Logger.getLogger(UploadFileServlet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Exception");
            Logger.getLogger(UploadFileServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("management.jsp");
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
