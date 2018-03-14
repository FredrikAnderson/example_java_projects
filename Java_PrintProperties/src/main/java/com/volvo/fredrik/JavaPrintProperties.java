package com.volvo.fredrik;

import java.util.Enumeration;
import java.util.Properties;

public class JavaPrintProperties {

    public static void main(String[] args) {
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
