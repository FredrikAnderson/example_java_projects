package com.volvo.jenkins.application.infra.cmd;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Environment implements DeploymentConfigList, DeploymentConfig {

    List<DeploymentConfig> envList = new ArrayList<DeploymentConfig>();
    
    public Environment(EnvironmentVariable... envs) {
        envList = Arrays.asList(envs);
    }

    @Override
    public List<DeploymentConfig> getConfigList(String env) {
        return envList;
    }

    @Override
    public String toString() {
        return "Environment, size: " + envList.size();
    }

    @Override
    public String getCommand(String env) {
        return null;
    }    
    
}
