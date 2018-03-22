package com.fredrikanderson.script_execution_java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.text.StringSubstitutor;

/**
 * Hello world!
 *
 */
public class App {

    private Map<String, String> valuesMap = new HashMap<String, String>();
    
    public static void main(String[] args) {
        App app = new App(args);
        app.test();
                
        app.executeScript(args);
        
    }
    
    private App(String[] args) {

//        valuesMap.put("animal", "quick brown fox");
//        valuesMap.put("target", "lazy dog");

        // Adding java properties
        valuesMap.putAll(System.getProperties().entrySet().stream()
                   .collect(Collectors.toMap(e -> e.getKey().toString(), e -> e.getValue().toString())));
        // Adding System environment variables
        valuesMap.putAll(System.getenv().entrySet().stream()
                         .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue())));
        
        // Adding arguments to values
        for (int i = 1; i < args.length; i++) {
            valuesMap.put(Integer.toString(i), args[i]);
        }
        
//        for (String key : valuesMap.keySet()) {
//            System.out.println(key + "=" + valuesMap.get(key));            
//        }
    }

    private void test() {
        
//        try {
//            String current = new File( "." ).getCanonicalPath();
//            System.out.println("Current dir:"+current);
//            String currentDir = System.getProperty("user.dir");
//            System.out.println("Current dir using System:" +currentDir);
//        } catch (IOException e1) {
//            e1.printStackTrace();
//        }
                
        String templateString = "The ${animal} jumps over the ${target}. Java version: ${java.version}. Path: ${COMPUTERNAME}. One: ${1}, Two: ${2}, Three: ${3}. Four: ${4} ";
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        String resolvedString = sub.replace(templateString);

        System.out.println("Str: " + resolvedString);        
    }
    
    private void executeScript(String[] args) {
        
        File inputFile = new File(args[0]);        
        try (BufferedReader br = new BufferedReader(new FileReader(inputFile))) {

            System.out.println("Reading message from file: " + inputFile.getAbsolutePath() + ", " + inputFile.toString() + 
                    ", " + inputFile.getCanonicalPath());
            
            String line = null;
            String ls = System.getProperty("line.separator");
            int nrLines = 0;
            while ((line = br.readLine()) != null) {
                System.out.println("Line is: " + line);
            
                // Do 
                subst
                
                if (line.startsWith("include")) {
                    includeStatement(line);
                }

            }
//            logger.info("Number of lines read from file: " + nrLines);
            // delete the last new line separator
//            toret.deleteCharAt(toret.length() - 1);
            
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    private String substitueValues(String line) {
        StringSubstitutor sub = new StringSubstitutor(valuesMap);
        String resolvedString = sub.replace(line);
        
        return resolvedString;
    }
    
    private static void includeStatement(String line) {

        
    }
    
    
}
