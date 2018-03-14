package com.volvo.drs.versiontool.web;


import java.io.IOException;
import java.io.PrintWriter;

import com.volvo.drs.versiontool.Settings;
 
public class VersionToolServlet extends javax.servlet.http.HttpServlet {

    private App app = new App();

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) 
            throws javax.servlet.ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String deployToEnv = request.getParameter("deploy_to_env");
        String buildVersion = request.getParameter("build_version");
        out.println("<HTML>");
        out.println("<HEAD><TITLE></TITLE></HEAD>");
        
        out.println("<BODY>");
        out.println("Starting deploy to:" + deployToEnv + ", with build version: " + buildVersion + ".");
        out.println("</BODY></HTML>");
    }
 
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) 
            throws javax.servlet.ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html>");
        out.println("<head><title></title></head>");
        
        out.println("<body>");
        String environmentStatus = app.retrieveEnvironmentStatus();
        environmentStatus = environmentStatus.replace("\n", "<br>");
        out.println("<b>" + environmentStatus + "</b>");
        out.println("<br><br>");

        out.println("<form method=port action=\"version\"><P>");
        out.println("<table><tr>");        
        out.println("<td>What environment do you want to deploy:</td>");
        out.println("<td><INPUT TYPE=TEXT NAME=\"deploy_to_env\" VALUE=\"" + "dev" + "\"></td></tr>");

        out.println("<tr><td>What version of the SW do you want to deploy:</td>");
        out.println("<td><INPUT TYPE=TEXT NAME=\"build_version\" VALUE=\"" + "4.3" + "\"></td></tr>");

        out.println("<tr><td><input type=submit><td></tr>");
        out.println("</table>");        
        out.println("</form>");
        
        out.println("<pre>");
        out.println(Settings.getInstance().projectsToHTML());        
        out.println("</pre>");
        
        out.println("</body></html>");
        
    }
}