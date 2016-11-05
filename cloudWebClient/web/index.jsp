<%--
    Document   : index.jsp
    Created on : 09-09-2016, 0:26:54
    Author     : martin
--%>

<%@page import="java.net.InetAddress"%>
<%@page import="java.io.IOException"%>
<%@page import="javax.swing.JOptionPane"%>
<%@page import="java.net.Socket"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/cssIndex.css" media="screen">
        <title>Login ElectroCloud</title>
    </head>
    <body>

        <%
            /*
            // Funciona pero cuando no cierro sesion y el servidor no esta corriendo
            // causa errores, considerar reconectar para ese caso
            if(session.getAttribute("cliPackage") != null){
                response.sendRedirect("management.jsp");
                return;
            }
             */
        %>

        <div class="formLogin">
            <form class="form" action="login.do" method="post">
                <label for="txtNick"> Nick: </label>
                <input type="text" name="txtNick" required="required">
                <br>
                <label for="txtPass">Contraseña: </label>
                <input type="password" name="txtPass" required="required">
                <br>
                <input type="submit" name="btnProcesar" value="Ingresar">
            </form>
            <br>
            <label> ¿No tiene cuenta?
                <a href="register.jsp">Regístrese aquí</a>
            </label>
        </div>

        <div class="title">
            <h1>Bienvenido a Electro Cloud</h1>
        </div>

    </body>
</html>
