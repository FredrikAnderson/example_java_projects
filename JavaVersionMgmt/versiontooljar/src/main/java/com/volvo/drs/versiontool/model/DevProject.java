package com.volvo.drs.versiontool.model;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A DevProject models a Java development project with its corresponding attributes.
 */
public class DevProject {

    private String name = new String();
    
    private Collection<DeploymentEarFile> projectEarFiles = new ArrayList<DeploymentEarFile>();
    private Collection<VolvoEnv> volvoEnvs = new ArrayList<VolvoEnv>();

    private Collection<JenkinsProject> jenkinsBuildProjects = new ArrayList<JenkinsProject>();

    public DevProject(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<JenkinsProject> getJenkinsBuildProjects() {
        return jenkinsBuildProjects;
    }

    public void setJenkinsBuildProjects(Collection<JenkinsProject> jenkinsBuildProjects) {
        this.jenkinsBuildProjects = jenkinsBuildProjects;
    }

    public void addJenkinsBuildProject(JenkinsProject jenkinsBuildProject) {
        this.jenkinsBuildProjects.add(jenkinsBuildProject);
    }

    public Collection<DeploymentEarFile> getProjectEarFiles() {
        return projectEarFiles;
    }

    public void setProjectEarFiles(Collection<DeploymentEarFile> projectEarFiles) {
        this.projectEarFiles = projectEarFiles;
    }

    public void addProjectEarFile(DeploymentEarFile projectEarFile) {
        projectEarFiles.add(projectEarFile);
    }

    public Collection<VolvoEnv> getVolvoEnvs() {
        return volvoEnvs;
    }

    public void addVolvoEnv(VolvoEnv jbossEnv) {
        this.volvoEnvs.add(jbossEnv);
    }

    public void setVolvoEnvs(Collection<VolvoEnv> jbossEnvs) {
        this.volvoEnvs = jbossEnvs;
    }

}
