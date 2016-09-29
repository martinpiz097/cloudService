<%--
    Document   : userInfo.jsp
    Created on : 14-09-2016, 21:04:08
    Author     : martin
--%>

<%@page import="org.martin.cloudCommon.system.Converter"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="org.martin.cloudCommon.model.packages.ClientPackage"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
          <link rel="stylesheet" href="css/cssUserInfo.css" media="screen">
        <title>JSP Page</title>
    </head>
    <body>
        <div class="title">
            <h1>Información del Usuario</h1>
        </div>

        <%
            if(session.getAttribute("cliPackage") == null ||
                    session.getAttribute("connector") == null){
                response.sendRedirect("index.jsp");
                return;
            }
        %>

        <div class="infoUser">
          <div class="subTitles">
            <label class="subTitle">Espacio Utilizado:</label>
            <br>
            <label class="subTitle">Espacio Disponible:</label>
            <br>
            <label class="subTitle">Espacio Total:</label>
            <br>
            <label class="subTitle">Fecha de creación:</label>
          </div>

            <%
            ClientPackage cp = (ClientPackage)request.getSession().getAttribute("cliPackage");
            NumberFormat nf = new DecimalFormat("#0.00");

            %>

          <div class="datos">
              <label class = "dato"><%out.println(Converter.getConvertedSize(
                      cp.getAccount().getUsedSpace()));%></h3>
              <br>
              <label class = "dato"><%out.println(Converter.getConvertedSize(
                      cp.getAccount().getFreeSpace()));%></h3>
              <br>
              <label class = "dato"><%out.println(Converter.getConvertedSize(
                      cp.getAccount().getTotalSpace()));%></h3>
              <br>
              <label class = "dato"><%out.println(cp.getDateToString());%></h3>
          </div>
        </div>
        <div class="goBack">
          <a href="management.jsp">Volver al Panel</a>
        </div>
    </body>
</html>
