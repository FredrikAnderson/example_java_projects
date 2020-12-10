package com.fredrik.bookit.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebServlet(
  name = "Application Info Servlet",
  description = "App Info Servlet",
  urlPatterns = { "/support" }
)
public class AppInfoServlet extends HttpServlet {  
  
    private static final long serialVersionUID = 1L;

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    private static final String CONTENT_TYPE = "text/html;charset=UTF-8";

    /**
     * @see HttpServlet#HttpServlet()
     */
    public AppInfoServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            response.setContentType(CONTENT_TYPE);

            StringBuilder markup = new StringBuilder();            		
            
            String htmlPage = "<!DOCTYPE html>" + 
            "<html><head>" + 
            "<meta charset=\"ISO-8859-1\">" + 
            "<title>Book IT</title>" + 
            "</head>" + 
            "<style>" + 
            "body {" + 
            "	background-color: #ADCDE8;" + 
            "}" + 
            "</style>" + 
            "<body><center>" + 
            "" + 
            "	<h2>Book IT application</h2> " + 
            "	Documentation of exposed API:<br>" + 
            "	<a href=\"swagger-ui.html\">REST API with Swagger UI</a>" + 
            "	<br><br><br>" + 
            "	Application supportive functions:" + 
            "<!-- 	<br><a href=\"/loggers\">Loggers</a> -->" + 
            "<!-- 	<br><a href=\"/beans\">Beans</a> -->" + 
            "	<br><a href=\"/actuator/\">Spring Actuator</a>" + 
            "	<br><a href=\"/api/environment\">Environment</a>" + 
            "	<br><a href=\"/api/properties\">Properties</a>" + 
            "	<br><br>" + 
            "	<a href=\"/monitoring\">Application Monitoring with JavaMelody.</a>" + 
            "	<br><br>" + 
            "	<hr width=\"40%\">" + 
            "	If you have any questions, issues or improvement ideas, please send an email to: <br>" + 
            "	<a href=\"mailto:fanderson75@gmail.com\">Fredrik Andersson</a><br>" + 
            "" + 
            "	<br><br>" + 
            "	<hr width=\"40%\">" + 
            "" + 
            "</center></body></html>";
            
            markup.append(htmlPage);

            out.write(markup.toString());
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = null;
        try {
            out = response.getWriter();
            response.setContentType(CONTENT_TYPE);

            out.write("<html><head></head><body>");
            out.write("<center>");
            out.write("<br><br>");            
            
//            final String msgText = request.getParameter("msgText");
//            OAuthToken.getInstance().setToken(msgText);

            out.write("Token saved successfully.");
            
            out.write("</center>");
            out.write("</body></html>");
            
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            if (out != null) {
                out.flush();
                out.close();
            }
        }
    }

}