<%--
    Document   : management.jsp
    Created on : 10-09-2016, 15:06:51
    Author     : martin
--%>

<%@page import="org.martin.cloudWebClient.model.WebTable"%>
<%@page import="org.martin.cloudWebClient.model.TMFiles"%>
<%@page import="org.martin.cloudCommon.system.Command"%>
<%@page import="org.martin.cloudWebClient.net.Connector"%>
<%@page import="java.util.Date"%>
<%@page import="org.martin.cloudCommon.system.Utilities"%>
<%@page import="org.martin.cloudCommon.system.Converter"%>
<%@page import="java.io.File"%>
<%@page import="org.martin.cloudCommon.model.packages.ClientPackage"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/cssManagement.css" media="screen" title="no title" charset="utf-8">
        <title>Panel de Control</title>
    </head>
    <body>

        <%
            if(session.getAttribute("cliPackage") == null ||
                    session.getAttribute("connector") == null){
                response.sendRedirect("index.jsp");
                return;
            }
            ClientPackage cp = (ClientPackage)session.getAttribute("cliPackage");

            if(request.getParameter("m") != null){
                if(Integer.parseInt(request.getParameter("m")) == 1){
                    Connector con = (Connector)session.getAttribute("connector");

                    con.sendCommand(new Command("@access", cp.getCurrentDir().getCanonicalPath()));
                    Object obj = con.getReceivedObject();
                    cp.setCurrentDir((File) obj);

                    session.setAttribute("cliPackage", cp);
                }
            }

        %>

        <div class="title">
          <h1 class="tit">Electro</h1>
          <img class="tit" src="images/thunder.png" alt="thunder" width="128" height="128"/>
          <h1 class="tit">Cloud</h1>
        </div>

        <div class="menu">
            <a class="menuOP" href="#"><button class="btnMenu" type="button" name="button">File &nbsp;</button></a>
            <a class="menuOP" href="userInfo.jsp"><button class="btnMenu" type="button" name="button"> Info&nbsp;</button></a>
            <a class="menuOP" href="closeSession.do"><button class="btnMenu" type="button" name="button">Cerrar Sesión &nbsp;</button></a>
        </div>
        <br>

        <!----------------------------------------------------->


        <div class="panelInfo">

          <div class="buttons">
            <a class="linkButton" href="goBack.do">
              <button class="buttonPanel" type="button" name="button">
                <img class="imgBtn" src="images/goBack.png" alt="goBack" width="32" height="32"/>
              </button>
            </a>

            <a class="linkButton" href="home.do">
              <button class="buttonPanel" type="button" name="button">
                <img class="imgBtn" src="images/home.png" alt="home" width="32" height="32"/>
              </button>
            </a>

            <a class="linkButton" href="update.do">
              <button class="buttonPanel" type="button" name="button">
                <img class="imgBtn" src="images/update.png" alt="update" width="32" height="32"/>
              </button>
            </a>

            <a class="linkButton" href="uploadFiles.jsp">
              <button class="buttonPanel" type="button" name="button">
                <img class="imgBtn" src="images/upload.png" alt="upload" width="32" height="32"/>
              </button>
            </a>

            <a class="linkButton" href="newFolder.jsp">
              <button class="buttonPanel" type="button" name="button">
                <img class="imgBtn" src="images/newFolder.png" alt="newFolder" width="32" height="32"/>
              </button>
            </a>
          </div>

          <div class="infoFolder">
              <label id="lblCurrentFolder"> Carpeta actual:&nbsp;
                  <%out.println(cp.getCurrentDir().getName()); %>
              </label>
            <br>
          </div>

            <div class="info">
              <%
                  out.println("<label class = 'lblInfo'>Nombre de Usuario:&nbsp;"+cp.getUserNick()+"</label>");
                  double usedSpace = cp.getUsedSpaceInMB();
                  String strUsedSpace = Converter.getFormattedDecimal(usedSpace);
                  String totalSpace = Converter.getFormattedDecimal(cp.getTotalSpaceInMB()/1024);

                  if (usedSpace / 1024 < 1)
                      out.println("<label class = 'lblInfo'> Espacio Utilizado:&nbsp;"+
                              strUsedSpace+" MB/"+totalSpace+"GB</label>");
                  else
                      out.println("<label class = 'lblInfo'> Espacio Utilizado:&nbsp;"+
                              strUsedSpace+" GB/"+totalSpace+"GB</label>");

                  totalSpace = null;
              %>
            </div>

            <br/>

        </div>

        <div class="panelManager">
            <div class="dirs">
                <label id = "titFolders">Carpetas</label>
                <br>
                <%
                    File[] dirs = cp.getOrderedDirs();
                    if(dirs != null){
                        for(File dir : dirs){
                            out.println("<a class 'folder' href='access.do?folder="+dir.getCanonicalPath()+"'>");

                            out.println("<button class = 'btnFolder' type='button' name='btnDir'>");

                            out.println("<img src='images/folder1.png' alt='fld'"
                                    + "width = '32' height = '32'/>");
                            out.println(dir.getName());
                            out.println("</button>");

                            out.println("</a>");

                            out.println("<br>");
                        }
                    }
                %>

            </div>

            <div class="files">
                <label>Archivos</label>
                <%
                    File[] files = cp.getOrderedFiles();
                    if (files != null) {
                        TMFiles model = new TMFiles(files);
                        WebTable<File> table = new WebTable<>(out, "tblFiles", "tbl", (byte)1);
                        table.setModel(model);
                        table.draw();
                    }
                %>

            </div>

        </div>
    </body>
</html>
