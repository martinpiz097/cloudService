/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudWebClient.servlets;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;
import org.martin.cloudCommon.system.Archive;
import org.martin.cloudCommon.system.Command;
import org.martin.cloudWebClient.net.Connector;

/**
 *
 * @author martin
 */
@WebServlet(name = "DownloadFileServlet", urlPatterns = {"/downloadFile.do"})
public class DownloadFileServlet extends HttpServlet {

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
        
        try {
            HttpSession session = request.getSession();
            
            if (session.getAttribute("cliPackage") == null ||
                    session.getAttribute("connector") == null) {
                response.sendRedirect("index.jsp");
                return;
            }
            
            Connector con = (Connector) session.getAttribute("connector");
            String filePath = request.getParameter("file");
            File remoteFile = new File(filePath);
            con.sendCommand(Command.dwnF.getOrder(), remoteFile.getName(), filePath);
            Archive toDownload = (Archive) con.getReceivedObject();
            JOptionPane.showMessageDialog(null, "Nombre del archivo a descargar: "+toDownload.getName());
            
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + remoteFile.getName());
            try (ServletOutputStream outputStream = response.getOutputStream()) {
                outputStream.write(toDownload.getQueueBytes().poll());
                outputStream.flush();
                outputStream.close();
            }
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DownloadFileServlet.class.getName()).log(Level.SEVERE, null, ex);
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
        processRequest(request, response);
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
