package com.volvo.drs.versiontool.model;

/**
 * JenkinsBuildProject, describing a Jenkins build project.
 */
public class DeploymentEarFile {

    private String projectInternalEarFileName = new String();
    
    private String deploymentEarFileName = new String();
    
    public DeploymentEarFile(String projectInternalEarFileName, String deploymentEarFileName) {
        this.setProjectInternalEarFileName(projectInternalEarFileName);
        this.setDeploymentEarFileName(deploymentEarFileName);
    }

    public String getProjectInternalEarFileName() {
        return projectInternalEarFileName;
    }

    public void setProjectInternalEarFileName(String projectInternalEarFileName) {
        this.projectInternalEarFileName = projectInternalEarFileName;
    }

    public String getDeploymentEarFileName() {
        return deploymentEarFileName;
    }

    public void setDeploymentEarFileName(String deploymentEarFileName) {
        this.deploymentEarFileName = deploymentEarFileName;
    }
    
}
