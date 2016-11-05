/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudWebClient.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.martin.cloudCommon.model.packages.ClientPackage;
import org.martin.cloudCommon.model.packages.UpdatePackage;
import org.martin.cloudCommon.system.Command;
import org.martin.cloudWebClient.net.Connector;

/**
 *
 * @author martin
 */
@WebServlet(name = "RenameFileServlet", urlPatterns = {"/renameFile.do"})
public class RenameFileServlet extends HttpServlet {

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
        String newName = request.getParameter("txtNewName");
        
        if (filePath == null || newName == null) {
            response.sendRedirect("management.jsp");
            return;
        }
        
        Connector con = (Connector) session.getAttribute("connector");
        ClientPackage cp = (ClientPackage) session.getAttribute("cliPackage");
        
        Command cmdRename = new Command("@rename");
        cmdRename.addOption(filePath);
        cmdRename.addOption(newName);
        con.sendCommand(cmdRename);
        con.sendUpdateRequest(cp.getCurrentDirPath(), cp.getUserNick());
        try {
            cp.update((UpdatePackage) con.getReceivedObject());
            session.setAttribute("cliPackage", cp);
            response.sendRedirect("management.jsp");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RenameFileServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        cmdRename = null;
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

        if (request.getSession().getAttribute("cliPackage") == null || 
                request.getSession().getAttribute("connector") == null)
            response.sendRedirect("index.jsp");
        
        else
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
