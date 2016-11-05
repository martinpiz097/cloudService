/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.martin.cloudWebClient.servlets;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;
import org.martin.cloudCommon.model.packages.ClientPackage;
import org.martin.cloudCommon.model.packages.UserPackage;
import org.martin.cloudCommon.system.Command;
import org.martin.cloudCommon.system.SysInfo;
import org.martin.cloudWebClient.net.Connector;
import org.martin.cloudWebClient.net.Tester;

/**
 *
 * @author martin
 */
@WebServlet(name = "LoginServlet", urlPatterns = {"/login.do"})
public class LoginServlet extends HttpServlet {
    private Connector connector;
    //private Socket socket;
    
    public LoginServlet() {
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("index.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final String nick = request.getParameter("txtNick");
        final String passw = request.getParameter("txtPass");
        
        if (nick.isEmpty() || passw.isEmpty())
            response.sendRedirect("index.jsp");
        
        else{
            try {
                UserPackage up;
                connector = new Connector(
                        new Socket(SysInfo.LOCALHOST, SysInfo.DEFAULT_PORT));
                
                connector.sendCommand(Command.newLoginU(nick, passw));
                up = (UserPackage) connector.getReceivedObject();
                if (!up.hasClientPackage()) {
                    JOptionPane.showMessageDialog(null, "Usuario y/o contraseña incorrectos");
                    response.sendRedirect("index.jsp");
                    connector.closeConnection();
                }
                else{
                    ClientPackage cliPack = up.getCliPackage();
                    connector.reinstanceStreams();
                    request.getSession().setAttribute("cliPackage", cliPack);
                    request.getSession().setAttribute("connector", connector);
                    response.sendRedirect("management.jsp");
                }
                
                up = null;
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
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
