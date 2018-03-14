package com.volvo.drs.versiontool.web;


import java.io.IOException;
import java.io.PrintWriter;
 
public class DeployServlet extends javax.servlet.http.HttpServlet {

    App app = new App();

    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String deploy_to_env = request.getParameter("deploy_to_env");
        String build_version = request.getParameter("build_version");
        out.println("<HTML>");
        out.println("<HEAD><TITLE></TITLE></HEAD>");
        
        out.println("<BODY>");
        out.println("Starting deploy to:" + deploy_to_env + ", with build version: " + build_version + ".");
        out.println("</BODY></HTML>");
        
//        response.getWriter().println(app.testDeploy());
    }
 
    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

//        String name = request.getParameter("name");
        out.println("<HTML>");
        out.println("<HEAD><TITLE></TITLE></HEAD>");
        
        out.println("<BODY>");
        out.println("<FORM METHOD=POST ACTION=\"deploy\"><P>");
        out.println("What environment do you want to deploy:");
        out.println("<INPUT TYPE=TEXT NAME=\"deploy_to_env\" VALUE=\"" + "dev" + "\"><P>");
        out.println("What version of the SW do you want to deploy:");
        out.println("<INPUT TYPE=TEXT NAME=\"build_version\" VALUE=\"" + "4.3" + "\"><P>");
        out.println("<INPUT TYPE=SUBMIT>");
        out.println("</FORM>");
        out.println("Hello, name!!");
        out.println("</BODY></HTML>");
        
//        response.getWriter().println(app.testDeploy());
    }
}