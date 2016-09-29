<%-- 
    Document   : index.jsp
    Created on : 16-09-2016, 13:25:14
    Author     : martin
--%>

<%@page import="java.io.InputStreamReader"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.net.URL"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <%
            URL url = new URL("http://icanhazip.com");
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
        %>
        <h1 align="center"><%out.println(reader.readLine());%></h1>
        <%
            reader.close();
            reader = null;
            url = null;
        %>
        
    </body>
</html>
