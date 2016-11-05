<%--
    Document   : uploadFiles.jsp
    Created on : 15-09-2016, 7:37:10
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
        <form class="" action="uploadFile.do" method="post" enctype="multipart/form-data">
          <input type="file" name="file" value="" required="true">
          <input type="submit" name="btnUpload" value="Subir Archivo">
        </form>
    </body>
</html>
