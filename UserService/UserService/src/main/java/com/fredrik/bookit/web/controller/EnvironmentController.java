package com.fredrik.bookit.web.controller;

import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fredrik.bookit.model.Version;

@RestController
@RequestMapping("/api")
public class EnvironmentController {

    @RequestMapping("/environment")
    public String environment() {
        StringBuilder buf = new StringBuilder();

		Map<String, String> env = System.getenv();
		for (String envName : env.keySet()) {
			// System.out.format("%s=%s%n", envName, env.get(envName));
            buf.append(String.format("%s=%s%n", envName, env.get(envName)));
		} 
		
        return buf.toString();
    }
    
    @RequestMapping("/properties")
    public String properties() {
        StringBuilder buf = new StringBuilder();
        
        Properties p = System.getProperties();
        Enumeration<Object> keys = p.keys();
        while (keys.hasMoreElements()) {
            String key = (String)keys.nextElement();
            String value = (String)p.get(key);
            // System.out.println(key + ": " + value);
            buf.append(key + ": " + value + "\n");            
        }

        return buf.toString();
    }
    
    @RequestMapping("/version")
    public Version version() {
        return new Version();
    }


}