package com.volvo.jenkins.application.infra.cmd;

public class DeploymentStrategy implements DeploymentConfig {

    private String deploymentStretegy;
    
    String cmd = "oc patch dc/%s --patch %s{%sspec%s:{%sstrategy%s:{%stype%s:%s%s%s}}}%s";
    
    public DeploymentStrategy(String depStrategy) {
        this.setDeploymentStretegy(depStrategy);
    }

    public String getDeploymentStretegy() {
        return deploymentStretegy;
    }

    public void setDeploymentStretegy(String deploymentStretegy) {
        this.deploymentStretegy = deploymentStretegy;
    }

    @Override
    public String getCommand(String env) {
        return String.format(cmd, env, getStartEndArgumentTag(), getInTag(), getInTag(), getInTag(), getInTag(),
                             getInTag(), getInTag(), getInTag(), deploymentStretegy, getInTag(), getStartEndArgumentTag());
    }

    String getStartEndArgumentTag() {
        String toret = "\"";
        // If not Windows we are assuming Linux
        if (!System.getProperty("os.name").startsWith("Windows")) {
            toret = "'";
        }        
        return toret;
    }

    String getInTag() {
        String toret = "\\\"";
        // If not Windows we are assuming Linux
        if (!System.getProperty("os.name").startsWith("Windows")) {
            toret = "\"";
        }        
        return toret;
    }

}
