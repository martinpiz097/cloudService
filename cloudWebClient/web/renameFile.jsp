<%--
    Document   : newFolder.jsp
    Created on : 15-09-2016, 3:13:19
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
        String filePath = request.getParameter("file");
        if(filePath == null){
            response.sendRedirect("management.jsp");
            return;
        }
        %>

        <h1>Hello World!</h1>
        <div id="formNewFolder">
          <h2>Creando una nueva carpeta</h2>
          <form action="renameFile.do" method="post">
            <label for="txtNewName">Nuevo nombre: </label>
            <input type="text" name="txtNewName" required="true">
            <input type="submit" name="btnNewName" value="Renombrar">
            <input type="hidden" name="file" <%out.println("value = '"+filePath+"'"); %>>
          </form>
        </div>

        <div class="link">
          <a href="management.jsp?m=1">Volver al Panel</a>
        </div>
    </body>
</html>
