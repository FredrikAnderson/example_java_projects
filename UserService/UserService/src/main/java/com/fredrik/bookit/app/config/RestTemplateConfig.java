package com.fredrik.bookit.app.config;

import org.apache.http.HttpHost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


//@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate() {
//	    SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//
//	    Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress("cloudpxgot1.srv.volvo.com", 8080));
//	    requestFactory.setProxy(proxy);

		HttpHost proxy = new HttpHost("cloudpxgot1.srv.volvo.com", 8080, "https");
		CloseableHttpClient httpClient = HttpClientBuilder.create()
//				.setProxy(proxy )
				.useSystemProperties()
				.setSSLHostnameVerifier(new NoopHostnameVerifier())
				.build();
	    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
	    
	    return new RestTemplate(requestFactory);
	}
}