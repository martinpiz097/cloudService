/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudWebClient.servlets;

import java.io.IOException;
import java.util.Enumeration;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.swing.JOptionPane;
import org.martin.cloudWebClient.net.Connector;

/**
 *
 * @author martin
 */
@WebServlet(name = "CloseSessionServlet", urlPatterns = {"/closeSession.do"})
public class CloseSessionServlet extends HttpServlet {

    HttpSession session;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    
        session = request.getSession();
        Connector con = (Connector) session.getAttribute("connector");
        con.sendCommand("@close");
        con.closeConnection();
        con = null;

        Enumeration<String> attributeNames = session.getAttributeNames();
    
        while (attributeNames.hasMoreElements())
            session.removeAttribute(attributeNames.nextElement());

        // Expirar sesion completamente
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Expires", "0");
        response.setDateHeader("Expires", -1);
        // Expirar sesion completamente
        response.sendRedirect("index.jsp");
        attributeNames = null;
        session = null;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        if (request.getSession().getAttribute("cliPackage") == null)
            response.sendRedirect("index.jsp");
        else
            processRequest(request, response);
        
    }

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
