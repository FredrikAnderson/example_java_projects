package com.volvo.drs.versiontool.model;

/**
 * JenkinsBuildProject, describing a Jenkins build project.
 */
public class JenkinsProject {


    private JenkinsProjectType projectType = JenkinsProjectType.BUILD;

    private String name = new String();

    private String jenkinsName = new String();
    
    private String buildProjectUrl = new String();
    
    public JenkinsProject() {
    }

    public JenkinsProject(String name, String jenkinsName) {
        this.name = name;
        this.jenkinsName = jenkinsName;
    }

    public JenkinsProject(String name, String jenkinsName, JenkinsProjectType type) {
        this.name = name;
        this.jenkinsName = jenkinsName;
        this.projectType = type;
    }

    public String getBuildProjectUrl() {
        return buildProjectUrl;
    }

    public void setBuildProjectUrl(String buildProjUrl) {
        this.buildProjectUrl = buildProjUrl;
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

    public JenkinsProjectType getProjectType() {
        return projectType;
    }

    public void setProjectType(JenkinsProjectType projectType) {
        this.projectType = projectType;
    }

    public String getJenkinsName() {
        return jenkinsName;
    }

    public void setJenkinsName(String jenkinsName) {
        this.jenkinsName = jenkinsName;
    }
}
