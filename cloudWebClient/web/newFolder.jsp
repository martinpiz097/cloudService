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
        <h1>Hello World!</h1>
        <div id="formNewFolder">
          <h2>Creando una nueva carpeta</h2>
          <form action="newFolder.do" method="post">
            <label for="txtNewFolder">Nombre de la nueva carpeta: </label>
            <input type="text" name="txtNewFolder" required="true">
            <input type="submit" name="btnNewFolder" value="Crear">
          </form>
        </div>

        <div class="link">
          <a href="management.jsp?m=1">Volver al Panel</a>
        </div>
    </body>
</html>
