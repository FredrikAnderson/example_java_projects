package com.fredrik.hello;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Version {

    private String version = null;

    public String getVersion() {
        retrieveVersion();

        return version;
    }

    private void retrieveVersion() {
        if (version == null) {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("META-INF/MANIFEST.MF");
            Properties prop = new Properties();
            try {
                prop.load(is);
            } catch (IOException ex) {
                System.out.println("Exception: " + ex);
            }
            //        System.out.println("Props: " + prop);
            version = prop.getProperty("Implementation-Version");
        }
    }
}