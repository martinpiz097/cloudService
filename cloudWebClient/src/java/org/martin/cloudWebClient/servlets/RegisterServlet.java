/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudWebClient.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;
import org.martin.cloudCommon.system.Command;
import org.martin.cloudCommon.system.SysInfo;
import org.martin.cloudWebClient.net.Connector;
import org.martin.cloudWebClient.net.Tester;

/**
 *
 * @author martin
 */
@WebServlet(name = "RegisterServlet", urlPatterns = {"/register.do"})
public class RegisterServlet extends HttpServlet {

    private Connector connector;
    private Socket socket;

    public RegisterServlet() {}

        
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
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet RegisterServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
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
        String nick = request.getParameter("newNick");
        String pass1 = request.getParameter("newPass1");
        String pass2 = request.getParameter("newPass2");
    
        if (nick.isEmpty() || pass1.isEmpty() || pass2.isEmpty())
            response.sendRedirect("register.jsp");
        
        else if (!pass1.equals(pass2))
            response.sendRedirect("register.jsp");
        
        else{
            try {
                if (!Tester.isConnected()) {
                    response.sendRedirect("index.jsp");
                    return;
                }
                
                // Esto se debe cambiar
                socket = new Socket(SysInfo.LOCALHOST, SysInfo.DEFAULT_PORT);
                connector = new Connector(socket);
                connector.sendCommand(Command.newRegU(nick, pass1));
                final Object objReceived = connector.getReceivedObject();
                if (objReceived instanceof Boolean){
                    if ((Boolean)objReceived){
                        JOptionPane.showMessageDialog(null, "Registro exitoso!");
                        response.sendRedirect("index.jsp");
                    }
                    else{
                        JOptionPane.showMessageDialog(null, 
                                "El nick no se encuentra disponible",
                                "Mensaje", JOptionPane.WARNING_MESSAGE);
                        response.sendRedirect("register.jsp");
                    }
                }
                connector.closeConnection();
                connector = null;
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(RegisterServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
