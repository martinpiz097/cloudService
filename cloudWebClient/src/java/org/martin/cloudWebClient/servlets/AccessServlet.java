/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudWebClient.servlets;

import java.io.File;
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
import org.martin.cloudCommon.system.Command;
import org.martin.cloudWebClient.net.Connector;

/**
 *
 * @author martin
 */
@WebServlet(name = "AccessServlet", urlPatterns = {"/access.do"})
public class AccessServlet extends HttpServlet {

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
    
        if (session.getAttribute("cliPackage") == null) {
            response.sendRedirect("index.jsp");
            return;
        }
        
        String folder = request.getParameter("folder");
    
        if (folder == null) {
            response.sendRedirect("index.jsp");
            return;
        }
        
        ClientPackage cp = (ClientPackage) session.getAttribute("cliPackage");
        Connector con = (Connector) session.getAttribute("connector");
        
        Command cmdAccess = new Command("@access", folder);
        con.sendCommand(cmdAccess);
        try {
            cp.setCurrentDir((File) con.getReceivedObject());
            session.setAttribute("cliPackage", cp);
            response.sendRedirect("management.jsp");
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(AccessServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("index.jsp");
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
