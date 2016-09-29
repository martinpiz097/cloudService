<%-- 
    Document   : index.jsp
    Created on : 17-09-2016, 12:54:20
    Author     : martin
--%>

<%@page import="org.martin.cloudWebServer.net.Server"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
        <%
            Server.newInstance();
            Server.getInstance().start();
        %>
    
</html>
