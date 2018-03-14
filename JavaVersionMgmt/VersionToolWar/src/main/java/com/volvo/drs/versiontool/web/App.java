package com.volvo.drs.versiontool.web;

import com.volvo.drs.versiontool.VersionTool;

 
public class App {

    private VersionTool tool = new VersionTool(false);
    
    public String retrieveEnvironmentStatus() {
//        return String.format("The time is %s", "2015-06-12");

        return tool.getEnvironmentStatus();
    }

    public String testDeploy() {
        return tool.testDeploy();
    }
}