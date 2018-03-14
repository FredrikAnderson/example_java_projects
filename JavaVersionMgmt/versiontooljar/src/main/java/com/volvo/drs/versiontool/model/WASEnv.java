package com.volvo.drs.versiontool.model;


/**
 * WASEnv, describing a Websphere application server environment.
 */
public class WASEnv implements RuntimeEnvironment {

    private String name = new String();
    
    private String versionUrl = new String();
    
    private String deployArea = new String();

    public WASEnv(String wasEnv) {
        name = wasEnv;
    }
    
    public String getVersionUrl() {
        return versionUrl;
    }

    public void setVersionUrl(String versionUrl) {
        this.versionUrl = versionUrl;
    }

    public String getDeployArea() {
        return deployArea;
    }

    public void setDeployArea(String deployArea) {
        this.deployArea = deployArea;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean supportsRestVersionService() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public RuntimeProduct getRuntimeProduct() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RuntimeStage getStage() {
        // TODO Auto-generated method stub
        return null;
    }
}
