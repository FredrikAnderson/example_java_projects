package com.volvo.jenkins.application.infra.cmd;

import java.util.List;

public interface DeploymentConfigList {

    List<DeploymentConfig> getConfigList(String env);
    
}
