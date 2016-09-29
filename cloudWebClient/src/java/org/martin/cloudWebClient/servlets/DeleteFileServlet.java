package org.martin.cloudWebClient.servlets;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;
import org.martin.cloudCommon.model.packages.ClientPackage;
import org.martin.cloudCommon.system.Command;
import org.martin.cloudWebClient.net.Connector;

/**
 *
 * @author martin
 */
@WebServlet(urlPatterns = {"/deleteFile.do"})
public class DeleteFileServlet extends HttpServlet {

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
        
        String filePath = request.getParameter("file");
        
        if (filePath == null) {
            response.sendRedirect("management.jsp");
            return;
        }
        
        Connector con = (Connector) session.getAttribute("connector");
        ClientPackage cp = (ClientPackage) session.getAttribute("cliPackage");
        
        Command delete = new Command(Command.delF.getOrder(), filePath);
        con.sendCommand(delete);
        con.sendUpdateRequest(cp.getCurrentDirPath(), cp.getUserNick());
        try {
            cp.update(con.getUpdatesReceived());
            session.setAttribute("cliPackage", cp);
            response.sendRedirect("management.jsp");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DeleteFileServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

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
        response.sendRedirect("management.jsp");
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
