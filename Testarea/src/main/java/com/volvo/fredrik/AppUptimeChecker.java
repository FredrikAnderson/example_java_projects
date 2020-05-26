package com.volvo.fredrik;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Helper servlet to show current log levels and apply new.
 */
public class AppUptimeChecker {

    private static final long serialVersionUID = 1L;

    private static final String CONTENT_TYPE = "text/html;charset=UTF-8";

    class OAuth2ServerInfo {
        public String url;
        public String grant_type;
        public String httpMethod;

    }

    public static void main(String[] args) {

    	
    	
        try {
        	
        	SystemTray tray = SystemTray.getSystemTray();
        	
//        	Image image = null;
        	Image image = Toolkit.getDefaultToolkit().getImage("images/greenball.png");
			TrayIcon trayIcon = new TrayIcon(image);

			PopupMenu popup = new PopupMenu();
			MenuItem exitMi = new MenuItem("Exit");
			exitMi.addActionListener(new ActionListener() {				
				@Override
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});
			popup.add(exitMi);
			trayIcon.setPopupMenu(popup);
			tray.add(trayIcon);
        	
            int status = requestURL("https://www.google.se");
            
//            requestOAuth2Token("https://testmanager-test.got.volvo.net/tm-uiservice/api/environment/version");
//            requestOAuth2Token("https://testmanager-qa.got.volvo.net/tm-uiservice/api/environment/version");
            // requestOAuth2Token("http://testmanager-capacity.got.volvo.net/tm-uiservice/api/environment/version");
            
            
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (AWTException e) {
			e.printStackTrace();
		}

    }

    private static int requestURL(String url) throws IOException {

        int toret = 0;

        info("Requesting HTTP get: " + url);

        CloseableHttpClient httpclient = null;
        CloseableHttpResponse getResponse = null;
        try {
            SSLContext sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, (certificate, authType) -> true).build();

            // CloseableHttpClient client = HttpClients.custom()
            // .setSSLContext(sslContext)
            // .setSSLHostnameVerifier(new NoopHostnameVerifier())
            // .build();

            // CloseableHttpClient httpclient = HttpClients.createDefault();

             httpclient = HttpClients.custom()
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

            HttpGet httpGet = new HttpGet(url);

            info(httpGet.toString());
            
            getResponse = httpclient.execute(httpGet);
            // The underlying HTTP connection is still held by the response object
            // to allow the response content to be streamed directly from the network socket.
            // In order to ensure correct deallocation of system resources
            // the user MUST call CloseableHttpResponse#close() from a finally clause.
            // Please note that if response content is not fully consumed the underlying
            // connection cannot be safely re-used and will be shut down and discarded
            // by the connection manager.

            toret = getResponse.getStatusLine().getStatusCode();
            
//            Header contentTypeHeader = getResponse.getFirstHeader("Content-Type");
//            info("HTTP response, statusLine: " + getResponse.getStatusLine() + ", " + contentTypeHeader);
//            HttpEntity entity1 = getResponse.getEntity();
//
//            // Headers
//            // Header[] headers = postResponse.getAllHeaders();
//            // for (Header h : headers) {
//            // info("addHeader: " + h.getName() + " - " + h.getValue());
//            // }
//            String entityContents = EntityUtils.toString(getResponse.getEntity());
//            ObjectMapper jsonmapper = new ObjectMapper();
//            JsonNode json = jsonmapper.readTree(entityContents);
//            info(json.get("Version-Number").asText());
//
//            // info("Content: " + entityContents);
//            // markup.append(entityContents);
//
//            // do something useful with the response body
//            // and ensure it is fully consumed
//            EntityUtils.consume(entity1);
            
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } finally {
            getResponse.close();
            httpclient.close();
        }
        return toret;
    }

    
    private static int requestOAuth2Token(String url) throws IOException {

        int toret = 0;

        // If localdev, use local instance of oauth2 server
        // Basic Auth - tm-uiservice
        //

        // info("Requesting version using HTTP get: " + httpGet.toString());

        CloseableHttpClient httpclient = null;
        CloseableHttpResponse postResponse = null;
        try {
            SSLContext sslContext = new SSLContextBuilder()
                    .loadTrustMaterial(null, (certificate, authType) -> true).build();

            // CloseableHttpClient client = HttpClients.custom()
            // .setSSLContext(sslContext)
            // .setSSLHostnameVerifier(new NoopHostnameVerifier())
            // .build();

            // CloseableHttpClient httpclient = HttpClients.createDefault();

             httpclient = HttpClients.custom()
                    .setSSLContext(sslContext)
                    .setSSLHostnameVerifier(new NoopHostnameVerifier()).build();

            HttpGet httpGet = new HttpGet(url);

            info(httpGet.toString());
            
            postResponse = httpclient.execute(httpGet);
            // The underlying HTTP connection is still held by the response object
            // to allow the response content to be streamed directly from the network socket.
            // In order to ensure correct deallocation of system resources
            // the user MUST call CloseableHttpResponse#close() from a finally clause.
            // Please note that if response content is not fully consumed the underlying
            // connection cannot be safely re-used and will be shut down and discarded
            // by the connection manager.

            toret = postResponse.getStatusLine().getStatusCode();
            Header contentTypeHeader = postResponse.getFirstHeader("Content-Type");
//            info("HTTP response, statusLine: " + postResponse.getStatusLine() + ", " + contentTypeHeader);
            HttpEntity entity1 = postResponse.getEntity();

            // Headers
            // Header[] headers = postResponse.getAllHeaders();
            // for (Header h : headers) {
            // info("addHeader: " + h.getName() + " - " + h.getValue());
            // }
            String entityContents = EntityUtils.toString(postResponse.getEntity());
            ObjectMapper jsonmapper = new ObjectMapper();
            JsonNode json = jsonmapper.readTree(entityContents);
            info(json.get("Version-Number").asText());

            // info("Content: " + entityContents);
            // markup.append(entityContents);

            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity1);
            
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } finally {
            postResponse.close();
            httpclient.close();
        }
        return toret;
    }

    private static void info(String string) {

        System.out.println("INFO: " + string);
    }

    private Object toHTML(Map<String, String> logLevelsMap) {
        StringBuilder sb = new StringBuilder();

        sb.append("<table>");
        for (String keyStr : logLevelsMap.keySet()) {
            sb.append("<tr>");
            sb.append("<td>" + keyStr + "</td>");
            sb.append("<td>" + logLevelsMap.get(keyStr) + "</td>");
            sb.append("</tr>");
        }
        sb.append("</table>");

        return sb.toString();
    }

    private void printApplyLogLevelForm(StringBuilder markup) {
        // Apply log level form
        markup.append("<h3>Apply new loglevel for a logger:</h3>");
        markup.append("<form method='post' action='logging'><table><tr>");
        markup.append("<td>Username:</td> <td><input type='text' name='username' id='usernameId' size='40'/></td>");
        markup.append("</tr><tr>");
        markup.append("<td>Log level:</td> <td><input type='text' name='password' id='passwordId' size='40'/></td>");
        markup.append("</tr></table>");
        markup.append("<button type='submit'>Apply loglevel</button></form>");
    }

    private void printCurrentDateAndTime(StringBuilder markup) {
        // Current date and time
        markup.append("<br>");

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh.mm.ss");
        String dateAndTimeWithFormat = formatter.format(today);

        markup.append(dateAndTimeWithFormat);
    }

}
