package com.volvo.jenkins.application.infra.cmd;

public class MemoryResources implements DeploymentConfig {

    private int requestInMb;

    private int limitInMb;

    String cmd = "oc set resources deploymentconfigs %s --requests=memory=%dMi --limits=memory=%dMi";
    
    public MemoryResources(int requestMb, int limitMb) {
        this.setRequestInMb(requestMb);
        this.setLimitInMb(limitMb);
    }

    public int getRequestInMb() {
        return requestInMb;
    }

    public void setRequestInMb(int requestInMb) {
        this.requestInMb = requestInMb;
    }

    public int getLimitInMb() {
        return limitInMb;
    }

    public void setLimitInMb(int limitInMb) {
        this.limitInMb = limitInMb;
    }

    @Override
    public String getCommand(String env) {
        return String.format(cmd, env, requestInMb, limitInMb);
    }

    @Override
    public String toString() {
        return "MemoryResources [requestInMb=" + requestInMb + ", limitInMb=" + limitInMb + "]";
    }    
    
    
}
