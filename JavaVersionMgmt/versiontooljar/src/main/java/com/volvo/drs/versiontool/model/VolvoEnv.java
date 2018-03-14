package com.volvo.drs.versiontool.model;

import com.volvo.drs.versiontool.model.RuntimeEnvironment.RuntimeProduct;
import com.volvo.drs.versiontool.model.RuntimeEnvironment.RuntimeStage;

/**
 * WASEnv, describing a Websphere application server environment.
 */
public class VolvoEnv implements RuntimeEnvironment {

    private String name = new String();
    
    private RuntimeStage stage = RuntimeStage.DEV;
    
    private RuntimeProduct runtimeProduct = RuntimeProduct.JBOSS;
        
    private String versionUrl = new String();
    
    private String deployArea = new String();

    private boolean supportsRestVersionService = true;

    public VolvoEnv(String envName) {
        this.name = envName;
    }

    public VolvoEnv(String envName, RuntimeStage stage, RuntimeProduct prod, boolean supportVersionRestService) {
        this.name = envName;
        this.stage = stage;
        this.runtimeProduct = prod;
        this.supportsRestVersionService = supportVersionRestService;
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
        return supportsRestVersionService;
    }

    public void setSupportsRestVersionService(boolean supportVersionService) {
        this.supportsRestVersionService = supportVersionService;
    }

    @Override
    public RuntimeProduct getRuntimeProduct() {
        return runtimeProduct;
    }

    @Override
    public RuntimeStage getStage() {
        return stage;
    }

    public void setStage(RuntimeStage stage) {
        this.stage = stage;
    }

    public void setRuntimeProduct(RuntimeProduct runtimeProduct) {
        this.runtimeProduct = runtimeProduct;
    }
}
