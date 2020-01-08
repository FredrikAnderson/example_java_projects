package com.volvo.fredrik;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Equator;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class TestingArea {
    
    public static void main(String[] args) {

        compareIntLists();
        
//        compareLists();
        
//        comparePersonLists();
        
//        Long long1 = new Long(2);
//        Long long2 = new Long(2);
//
//        if (long1 == long2) {
//            System.out.println("long1 and long2 ==");
//        }
//        if (long1.equals(long2)) {
//            System.out.println("long1 and long2 equals");
//        }
//
        
//        generateInsertStmt(args);

//        try {
//
//            testPingFederateAuth();
////            testHttpPostWithBasicAuth();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        
        
//        printJavaProperties(args);        
    }

    private static void compareIntLists() {

        ArrayList<Integer> list1 = new ArrayList<Integer>(); 
        
        // Add values in ArrayList 
        list1.add(new Integer(32)); 
        list1.add(new Integer(33)); 
        list1.add(new Integer(35)); 
  
        // print list 1 
        System.out.println("List1: " + list1); 

        // Create ArrayList list2 
        ArrayList<Integer> list2 = new ArrayList<Integer>(); 
        // Add values in ArrayList 
        list2.add(new Integer(44)); 
        list2.add(new Integer(33)); 
        list2.add(new Integer(35)); 

        // Create ArrayList list3
        ArrayList<Integer> list3 = new ArrayList<Integer>(); 
        // Add values in ArrayList 
        list3.add(new Integer(32)); 
        list3.add(new Integer(33)); 
        list3.add(new Integer(35)); 

       
//        list2.get(1).setAge(44);
////        list2.get(2).setLastName("Pettersson");
//        list2.get(2).setFirstName("Fredda");

        // print list 2 
        System.out.println("List2: " + list2); 

        System.out.println("List1 and list2 same: " + Arrays.deepEquals(list1.toArray(), list2.toArray())); 

        System.out.println("List1 and list3 same: " + Arrays.deepEquals(list1.toArray(), list3.toArray())); 

        list3.add(new Integer(3));

        System.out.println("List1 and list3 same: " + Arrays.deepEquals(list1.toArray(), list3.toArray())); 

    }

    private static void comparePersonLists() {
        
        // create ArrayList list1 
           ArrayList<Person> list1 = new ArrayList<Person>(); 
     
           // Add values in ArrayList 
           list1.add(new Person("Fred", "And", 32)); 
           list1.add(new Person("Petter", "Nilson", 33)); 
           list1.add(new Person("Fredrik", "Anderson", 35)); 
     
           // print list 1 
           System.out.println("List1: " + list1); 

           // Create ArrayList list2 
           ArrayList<Person> list2 = new ArrayList<Person>(); 

           // Add values in ArrayList 
           list2.add(new Person("Fred", "And", 44)); 
           list2.add(new Person("Petter", "Nilson", 33)); 
           list2.add(new Person("Fredda", "Anderson", 35)); 

          
//           list2.get(1).setAge(44);
////           list2.get(2).setLastName("Pettersson");
//           list2.get(2).setFirstName("Fredda");
           
           // print list 2 
           System.out.println("List2: " + list2); 

           System.out.println("List1: " + list1); 

//           ArrayList<Person> common = new ArrayList<>();
//           common.addAll(list1);
//           
//           // Find the common elements 
//           common.retainAll(list2); 
//           // print list 1 
//           System.out.println("Common elements: " + common); 
           
//           // Now we should have only unique items from list1, ie elements that was removed from list2
//           list1.removeAll(common);
//           System.out.println("Removed elements: " + list1); 
//
//           // Now we should have only unique items from list2, ie elements that was added to list2
//           list2.removeAll(common);
//           System.out.println("Added elements: " + list2); 
//

//           Collections
//           Predicate<Person> equals = 
//           System.out.println("Age over 33:");
//           List<Person> rest = common.stream().filter(item -> item.getAge() >= 33).collect(Collectors.toList());
//           rest.forEach(System.out::println);

           PersonFirstEquator equator = new PersonFirstEquator();
           Collection<Person> retainAll = CollectionUtils.retainAll(list1, list2, Person.FIRSTNAME_EQUATOR);

           System.out.println("Equator:");
           retainAll.forEach(System.out::println);
           
       }

    private static void compareLists() {
        
        
     // create ArrayList list1 
        ArrayList<String> 
            list1 = new ArrayList<String>(); 
  
        // Add values in ArrayList 
        list1.add("Hii"); 
        list1.add("Geeks"); 
        list1.add("for"); 
        list1.add("Geeks"); 
  
        // print list 1 
        System.out.println("List1: "
                           + list1); 
  
        // Create ArrayList list2 
        ArrayList<String> 
            list2 = new ArrayList<String>(); 
  
        // Add values in ArrayList 
        list2.add("Hii"); 
        list2.add("Geeks"); 
        list2.add("Gaurav"); 
  
        // print list 2 
        System.out.println("List2: "
                           + list2); 
  
        ArrayList<String> common = new ArrayList<>();
        common.addAll(list1);
        
        // Find the common elements 
        common.retainAll(list2); 
  
        // print list 1 
        System.out.println("Common elements: "
                           + common); 
        
        // Now we should have only unique items from list1, ie elements that was removed from list2
        list1.removeAll(common);

        System.out.println("List1 elements: "
                + list1); 

        // Now we should have only unique items from list2, ie elements that was added to list2
        list2.removeAll(common);

        System.out.println("List2 elements: "
                + list2); 

        
    }

    private static void generateInsertStmt(String[] args) {
        
        try {
            File file = new File("C:\\temp\\input.txt");
            Scanner scanner = new Scanner(file);
            boolean firstLine = true;
            String[] columns = new String[20];
            ArrayList<String> valueLines = new ArrayList<>();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
//                System.out.println("Line: " + line);
                if (firstLine) {
                    columns = line.split("\t");
                    firstLine = false;
                } else {
                    valueLines.add(line);
                }
            }
            scanner.close();
            
            int value = 0;
            for (String str : valueLines) {
                value++;
                System.out.println("<insert tableName=\"" + args[0] + "\">");
                String[] values = str.split("\t");
                int column = 0;
                System.out.println("   <column name=\"id\" value=\"" + value + "\"/>");
                System.out.println("   <column name=\"description\" value=\"\"/>");
                for (String string : values) {
                    System.out.println("   <column name=\"" + columns[column] + "\" value=\"" + forXml(string) + "\"/>");
                    column++;
                }
                System.out.println("</insert>");                
                
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        
        
    }

    private static String forXml(String string) {
        return string.replaceAll("&", "&amp;");
    }

    private static void testPingFederateAuth() throws ClientProtocolException, IOException {
                
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet("https://federate-qa.volvo.com/as/authorization.oauth2?client_id=tm-uiservice&response_type=token&redirect_uri=https://localhost");
        // Basic Auth - tm-uiservice
        //                     
//        String auth = "tm-uiservice:secret";
//        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.ISO_8859_1));
//        String authHeader = "Basic " + new String(encodedAuth);
//        httpGet.setHeader(HttpHeaders.AUTHORIZATION, authHeader);

        httpGet.addHeader("Upgrade-Insecure-Requests", "1");
        httpGet.addHeader("Connection", "keep-alive");
        httpGet.addHeader("Pragma", "no-cache");
        httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
        httpGet.addHeader("Accept-Encoding", "gzip, deflate, br");
        httpGet.addHeader("Accept-Language", "en-US,en;q=0.9");
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
        
        info("HTTP Get: " + httpGet.toString());

        CloseableHttpResponse getResponse = httpclient.execute(httpGet);
        // The underlying HTTP connection is still held by the response object
        // to allow the response content to be streamed directly from the network socket.
        // In order to ensure correct deallocation of system resources
        // the user MUST call CloseableHttpResponse#close() from a finally clause.
        // Please note that if response content is not fully consumed the underlying
        // connection cannot be safely re-used and will be shut down and discarded
        // by the connection manager. 
        try {
            info("StatusLine: " + getResponse.getStatusLine());
            HttpEntity entity1 = getResponse.getEntity();

            // Statusode
            info("" + getResponse.getStatusLine().getStatusCode());

            // Headers
            Header[] headers = getResponse.getAllHeaders();
            for (Header h : headers) {
//                LOGGER.info("Header:" + h.getName() + ": " + h.getValue());
                info("addHeader: " + h.getName() + " - " +  h.getValue());
            }

            String entityContents = EntityUtils.toString(getResponse.getEntity());
            
//            InputStream inputStream = entity1.getContent();
//            BufferedReader buf = new BufferedInputStream(inputStream)
            
            info("Content: " + entityContents);
            
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity1);
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            getResponse.close();
        }
    }

    private static void testHttpPostWithBasicAuth() throws ClientProtocolException, IOException {
        
        CredentialsProvider provider = new BasicCredentialsProvider();
        UsernamePasswordCredentials credentials
            = new UsernamePasswordCredentials("tm-uiservice", "secret");
        provider.setCredentials(AuthScope.ANY, credentials);
        
//        CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
        
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://localhost:8090/oauth/token?grant_type=password&username=testleader&password=testleader");
        // Basic Auth - tm-uiservice
        //                     
        String auth = "tm-uiservice:secret";
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.ISO_8859_1));
        String authHeader = "Basic " + new String(encodedAuth);
        httpPost.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
//        httpPost.addHeader("accept", "*/*");
//        httpPost.addHeader("host", "localhost:8090");
//        httpPost.addHeader("cache-control", "no-cache");

        Header[] headers = httpPost.getAllHeaders();
        for (Header h : headers) {
//            LOGGER.info("Header:" + h.getName() + ": " + h.getValue());
            info("addHeader: " + h.getName() + " - " +  h.getValue());
        }

//        String encoding = Base64.getEncoder().encodeToString("tm-uiservice:secret".getBytes());
//        httpPost.addHeader("Authorization", "Basic " + encoding);
//        httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");
        
        info("HTTP Post: " + httpPost.toString());

        CloseableHttpResponse postResponse = httpclient.execute(httpPost);
        // The underlying HTTP connection is still held by the response object
        // to allow the response content to be streamed directly from the network socket.
        // In order to ensure correct deallocation of system resources
        // the user MUST call CloseableHttpResponse#close() from a finally clause.
        // Please note that if response content is not fully consumed the underlying
        // connection cannot be safely re-used and will be shut down and discarded
        // by the connection manager. 
        try {
            info("StatusLine: " + postResponse.getStatusLine());
            HttpEntity entity1 = postResponse.getEntity();

            // Statusode
            info("" + postResponse.getStatusLine().getStatusCode());

            // Headers
            headers = postResponse.getAllHeaders();
            for (Header h : headers) {
//                LOGGER.info("Header:" + h.getName() + ": " + h.getValue());
                info("addHeader: " + h.getName() + " - " +  h.getValue());
            }

//            // Content-type and length
//            response.setContentType(response1.getEntity().getContentType().getValue());
//            response.setContentLength((int) response1.getEntity().getContentLength());
//            
//            // Content
//            IOUtils.copy(entity1.getContent(), response.getOutputStream());

            String entityContents = EntityUtils.toString(postResponse.getEntity());
            
//            InputStream inputStream = entity1.getContent();
//            BufferedReader buf = new BufferedInputStream(inputStream)
            
            info("Content: " + entityContents);
            
            // do something useful with the response body
            // and ensure it is fully consumed
            EntityUtils.consume(entity1);
        } catch (UnsupportedOperationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            postResponse.close();
        }
    }
    
    private static void info(String string) {
        System.out.println(string);
    }

    private static void printJavaProperties(String[] args) {
        System.out.println("Command-line arguments:");
        for (String arg : args) {
            System.out.println(arg);
        }
        
        Properties p = System.getProperties();
        Enumeration keys = p.keys();
        while (keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            String value = (String)p.get(key);
            System.out.println(key + ": " + value);
        }
    }

}
