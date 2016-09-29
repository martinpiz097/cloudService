<%-- 
    Document   : index.jsp
    Created on : 19-09-2016, 17:33:31
    Author     : martin
--%>

<%@page import="org.martin.cloudCommon.system.Archive"%>
<%@page contentType="text/xml" pageEncoding="UTF-8"%>
        <%
            out.println("<root>");
            /*for(int i = 0; i < 10; i++){
                out.println("<casa>");
                out.println("<id>"+(i+1)+"</id>");
                out.println("<ciudad>Ciudad"+(i+1)+"</ciudad>");
                out.println("</casa>");
            }
            */
            out.println("<file>");
            Archive archive = new Archive("/home/martin/Escritorio/1.jpg");
            archive.read();
            out.println("<name>"+archive.getName()+"</name>");
            out.println("<bytes>"+archive.writeToXML()+"</bytes>");
            out.println("</file>");
            
            out.println("</root>");
        %>
