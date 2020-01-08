package com.volvo.jenkins.application.infra.cmd;

public interface DeploymentConfig {

    String getCommand(String env);
    
}
