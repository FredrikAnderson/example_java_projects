package com.fredrik;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class Webscraper {

    static String hostName = null;
    static String port = null;
    static String user = null;
    static String pass = null;
    static String retryCount = "90";
    static String debug = null;

    public static void main(String[] args) {

        hostName = args[0];
        port = args[1];
        user = args[2];
        pass = args[3];
        retryCount = args[4];
        if (args.length > 5) {
            debug = args[5];
        }

        // url = "http://sidm2v.got.volvo.net/jboss/cgi-bin/restart.cgi";
        // user = "cs-ws-s-SID-JBOSSDev";
        // pass = "bc8%%c8e89c&eL&";
        // server = "segotl2419";
        //
        // url = "http://segotl1847.got.volvo.net/jboss/cgi-bin/restart.cgi";
        // user = "yt52878";
        // pass = "IawDRS18";
        // server = "segotl1847";
        //

        if (hostName == null || user == null || pass == null || port == null) {
            System.out.println("Some argument is null.");
            printArgs();
        }
        if ("debug".equals(debug)) {
            System.out.println("Debug enabled. Printing arguments.");
            printArgs();
        } else {

            checkJBossUp(hostName, port, user, pass, retryCount);
        }
    }

    private static void printArgs() {
        System.out.println("Arguments: url: " + hostName + ", user: " + user + ", pass:" + pass + ", server: " + port + ", debug: " + debug);
    }

    // HTTP GET request
    private static void checkJBossUp(String host, String port, String user, String pass, String retryCount) {

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(user, pass));
        CloseableHttpClient httpclient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
        CloseableHttpResponse response = null;

        int i = 1;
        int retries = Integer.parseInt(retryCount);
        boolean shouldCheck = true;
        while (shouldCheck  && i <= retries) {
            try {
                URI uri = URI.create("http://" + host + ":" + port + "/management?operation=attribute&name=server-state");
                HttpGet httpGet = new HttpGet(uri);

                System.out.println();
                System.out.println("Executing #" + i + " request as user: " + user + ": " + httpGet.getRequestLine());
                response = httpclient.execute(httpGet);

                System.out.println("----------------------------------------");
                // Response checking
                //
                String body = EntityUtils.toString(response.getEntity());
                int statusCode = response.getStatusLine().getStatusCode();
                String resultLine = null;                
                // Print stuff
                System.out.println(response.getStatusLine());
                System.out.println(body);
                
                if (statusCode == 200 && body.matches(".*running.*")) {

                    // result = JBossAvailabilityCheckResult.availabilityOk(String.format("Success: JBoss on %s is in state %s", hostname, body))
                    shouldCheck = false;
                } else if (statusCode == 401) {
                    resultLine = String.format("%s. Please check credentials for the JBoss you try to access (%s)", statusCode, host);
                } else if (statusCode > 226 && statusCode != 503) {
                    resultLine = String.format("HTTP Status: %s", statusCode);
                } else {
                    System.out.print(String.format("...attempt %s of %s: HTTP Status: %s, %s", i, retryCount, statusCode, body));
                }

            } catch (UnsupportedEncodingException e) {
                // e.printStackTrace();
            } catch (ClientProtocolException e) {
                // e.printStackTrace();
                System.out.println(e.getMessage());
            } catch (IOException e) {
                // e.printStackTrace();
                System.out.println(e.getMessage());
            } catch (Exception e) {
                // e.printStackTrace();
                System.out.println(e.getMessage());
            } finally {
                try {
                    if (response != null) {
                        response.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            // Next try
            if (shouldCheck) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {                    
                }
                i++;
                System.out.println();
            }
        }
        try {
            httpclient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        
    }

}
