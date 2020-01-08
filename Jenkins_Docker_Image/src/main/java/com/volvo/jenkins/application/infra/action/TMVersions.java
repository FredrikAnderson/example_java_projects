package com.volvo.jenkins.application.infra.action;

import java.io.IOException;

/**
 * Helper servlet to show current log levels and apply new.
 */
public class TMVersions {


    public static void main(String[] args) {

        info("");
        info("");
        info("Listing information about TM environments:");
        info("");

        try {
            requestOAuth2Token("dev", "https://testmanager-dev.got.volvo.net/tm-uiservice/api/environment/version");
            requestOAuth2Token("capacity", "https://testmanager-capacity.got.volvo.net/tm-uiservice/api/environment/version");
            requestOAuth2Token("test", "https://testmanager-test.got.volvo.net/tm-uiservice/api/environment/version");
            requestOAuth2Token("qa", "https://testmanager-qa.got.volvo.net/tm-uiservice/api/environment/version");
            requestOAuth2Token("prod", "https://testmanager.got.volvo.net/tm-uiservice/api/environment/version");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void requestOAuth2Token(String env, String url) throws IOException {
    	
//        CloseableHttpClient httpclient = null;
//        CloseableHttpResponse postResponse = null;
//        String version = "Unavailable";
//        
//        try {
//            SSLContext sslContext = new SSLContextBuilder()
//                    .loadTrustMaterial(null, (certificate, authType) -> true).build();
//
//             httpclient = HttpClients.custom()
//                    .setSSLContext(sslContext)
//                    .setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
//
//            HttpGet httpGet = new HttpGet(url);
//
//            info(env + ": (" + httpGet.getURI() + "):");
//            
//            postResponse = httpclient.execute(httpGet);
//            // The underlying HTTP connection is still held by the response object
//            // to allow the response content to be streamed directly from the network socket.
//            // In order to ensure correct deallocation of system resources
//            // the user MUST call CloseableHttpResponse#close() from a finally clause.
//            // Please note that if response content is not fully consumed the underlying
//            // connection cannot be safely re-used and will be shut down and discarded
//            // by the connection manager.
//
//            HttpEntity entity1 = postResponse.getEntity();
//
//            String entityContents = EntityUtils.toString(postResponse.getEntity());
//            ObjectMapper jsonmapper = new ObjectMapper();
//            JsonNode json = jsonmapper.readTree(entityContents);
//            
//            version = json.get("Version-Number").asText();
//
//            // do something useful with the response body
//            // and ensure it is fully consumed
//            EntityUtils.consume(entity1);
//            
//        } catch (Exception excep) {
//            info("Exception: " + excep.getMessage());
//        } finally {
//            postResponse.close();
//            httpclient.close();
//            
//            info(version);
//            info("");
//        }
    	
    }

    private static void info(String string) {
        System.out.println("" + string);
    }
}
