package com.volvo.jenkins.application.infra.cmd;

public class CpuResources implements DeploymentConfig {

    private int requestMilliCore;
    
    private int limitMilliCore;
    
    String cmd = "oc set resources deploymentconfigs %s --requests=cpu=%dm --limits=cpu=%dm";
    
    public CpuResources(int requestMilliCore, int limitMilliCore) {
        this.setRequestCpu(requestMilliCore);
        this.setLimitCpu(limitMilliCore);
        
    }

    public int getRequestCpu() {
        return requestMilliCore;
    }

    public void setRequestCpu(int requestCpu) {
        this.requestMilliCore = requestCpu;
    }

    public int getLimitCpu() {
        return limitMilliCore;
    }

    public void setLimitCpu(int limitCpu) {
        this.limitMilliCore = limitCpu;
    }

    @Override
    public String getCommand(String env) {
        return String.format(cmd, env, requestMilliCore, limitMilliCore);
    }

    @Override
    public String toString() {
        return "CpuResources [requestMilliCore=" + requestMilliCore + ", limitMilliCore=" + limitMilliCore + "]";
    }
    
}
