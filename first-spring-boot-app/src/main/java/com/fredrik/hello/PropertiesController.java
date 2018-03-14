package com.fredrik.hello;

import java.util.Enumeration;
import java.util.Properties;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PropertiesController {

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
}