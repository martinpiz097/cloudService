<%--
    Document   : register.jsp
    Created on : 10-09-2016, 15:06:35
    Author     : martin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            if(request.getParameter("r") == null)
                response.sendRedirect("linkServer.do?page=register.jsp&access=001");
        %>
        
        <h1>Registro de usuarios</h1>
        <form class="form" action="register.do" method="post">
          <label for="newNick"> Nick: </label>
          <input type="text" name="newNick" required="">

          <label for="newPass1"> Contraseña: </label>
          <input type="password" name="newPass1" required="">

          <label for="newPass2"> Repita Contraseña: </label>
          <input type="password" name="newPass2" required="">

          <input type="submit" name="btnRegistrar" value="Registrar Usuario">
        </form>
    </body>
</html>
