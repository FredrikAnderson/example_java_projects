package com.volvo.drs.versiontool.model;

/**
 * Environment status.
 */
public class EnvStatus {

    private int statusCode = 0;
    private boolean up = false;
    private String version = "";
    private Integer scmVersion = 0;

    public EnvStatus() {
    }
    
    public EnvStatus(boolean up, String version, Integer scmVersion) {
        this.up = up;
        this.version = version;
        this.scmVersion = scmVersion;
    }
    
    public boolean isUp() {
        return up;
    }
    
    public void setUp(boolean up) {
        this.up = up;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        
        setUp(statusCode == 200);
    }

    public Integer getScmRevision() {
        return scmVersion;
    }

    public void setScmVersion(Integer scmVersion) {
        this.scmVersion = scmVersion;
    }
}
