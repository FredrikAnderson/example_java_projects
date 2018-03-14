package com.volvo.drs.versiontool;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.volvo.drs.versiontool.model.DevProject;
import com.volvo.drs.versiontool.model.JenkinsProject;
import com.volvo.drs.versiontool.model.JenkinsProjectType;
import com.volvo.drs.versiontool.model.VolvoEnv;
import com.volvo.drs.versiontool.model.WASEnv;
import com.volvo.drs.versiontool.model.RuntimeEnvironment.RuntimeProduct;
import com.volvo.drs.versiontool.model.RuntimeEnvironment.RuntimeStage;

/**
 * Environment status.
 */
public class Settings {

    private Boolean checkNewJenkinsBuilds = Boolean.FALSE;
    private String localTempDir = "C:\\Temp\\";

    private Collection<DevProject> projects = new ArrayList<DevProject>();

    private Collection<VolvoEnv> userSelectedEnvsToCheck = new ArrayList<VolvoEnv>();
    
    private static Settings myInstance = null;

    private static String vcnPassword = null;

    public Settings() {
        addProjects();
        userSelectedEnvsToCheck.addAll(getCurrentProject().getVolvoEnvs());
    }
    
    public static Settings getInstance() {
        if (myInstance == null) {
            myInstance = new Settings();
        }
        return myInstance;
    }

    public static String getVCNUserName() {
        return System.getProperty("user.name");
    }

    public static String getVCNPassword() {
        vcnPassword =  "IawDRS34";
        
        if (vcnPassword == null) {
            JPasswordField pf = new JPasswordField();
            int okCxl = JOptionPane.showConfirmDialog(null, pf, "Please give VCN password to get info from Jenkins", JOptionPane.OK_CANCEL_OPTION,
                                                      JOptionPane.PLAIN_MESSAGE);
            if (okCxl == JOptionPane.OK_OPTION) {
                vcnPassword = new String(pf.getPassword());
            }
        }
        return vcnPassword;
    }

    public void addProjects() {
        addDRSProjects();
//        addProtomProjects();
    }

    private void addDRSProjects() {
        DevProject devProject = new DevProject("DRS Java");
//        devProject.addProjectEarFile(new DeploymentEarFile("/com.volvo.protom$ProtomEAR", "ProtomEAR.ear"));
//        devProject.addProjectEarFile(new DeploymentEarFile("/com.volvo.protom$ProtomDCEAR", "ProtomDCEAR.ear"));

        JenkinsProject jenkinskCheckinBuild = new JenkinsProject("DRS Pos", "Part Order System - Build JBoss");
        jenkinskCheckinBuild.setBuildProjectUrl("http://hudson.it.volvo.net/job/Part%20Order%20System%20-%20Build%20JBoss/");
        devProject.addJenkinsBuildProject(jenkinskCheckinBuild);

        JenkinsProject jenkinskDeployBuild = new JenkinsProject("DRS Pos, Deploy", "Part Order System - Deploy JBoss", 
                                                                JenkinsProjectType.DEPLOY);
        jenkinskDeployBuild.setBuildProjectUrl("http://hudson.it.volvo.net/job/Part%20Order%20System%20-%20Deploy%20JBoss/");
        devProject.addJenkinsBuildProject(jenkinskDeployBuild);

        VolvoEnv devEnv = new VolvoEnv("Dev", RuntimeStage.DEV, RuntimeProduct.JBOSS, true);
        devEnv.setVersionUrl("http://segotl1847.got.volvo.net:8080/pos-uiservice-rest/api/versions");
        devProject.addVolvoEnv(devEnv);

        VolvoEnv testEnv = new VolvoEnv("Test", RuntimeStage.TEST, RuntimeProduct.JBOSS, true);
        testEnv.setVersionUrl("http://segotl1848.got.volvo.net:8080/pos-uiservice-rest/api/versions");
        devProject.addVolvoEnv(testEnv);
        
        projects.add(devProject);
    }

//    private void addProtomProjects() {
//        DevProject devProject = new DevProject("ProTOM");
//        WASEnv test = new WASEnv("ST");
//        test.setDeployArea("http://wi30ihs00.got.volvo.net:2080/dropzone/protom/int/");
//        test.setVersionUrl("http://protom-test.got.volvo.net/ProtomUIService/version");
//        devProject.addWASEnv(test);
//
//        WASEnv test2 = new WASEnv("ST2");
//        test2.setDeployArea("http://wi30ihs00.got.volvo.net:2080/dropzone/protom/int2/");
//        test2.setVersionUrl("http://protom-test2.got.volvo.net/ProtomUIService/version");
//        devProject.addWASEnv(test2);
//
//        // WASEnv edu = new WASEnv("EDU");
//        // edu.setDeployArea("http://wv31ihs00.got.volvo.net:2080/dropzone/protom/val2/");
//        // edu.setVersionUrl("http://protom-edu.got.volvo.net/ProtomUIService/version");
//        // devProject.addWASEnv(edu);
//
//        WASEnv qa = new WASEnv("QA");
//        qa.setDeployArea("http://wv31ihs00.got.volvo.net:2080/dropzone/protom/val/");
//        qa.setVersionUrl("http://protom-qa.got.volvo.net/ProtomUIService/version");
//        devProject.addWASEnv(qa);
//
//        devProject.addProjectEarFile(new DeploymentEarFile("/com.volvo.protom$ProtomEAR", "ProtomEAR.ear"));
//        devProject.addProjectEarFile(new DeploymentEarFile("/com.volvo.protom$ProtomDCEAR", "ProtomDCEAR.ear"));
//
//        JenkinsProject jenkinskCheckinBuild = new JenkinsProject("ProtusTOM", "");
//        jenkinskCheckinBuild.setBuildProjectUrl("http://hudson.it.volvo.net/job/ProtusTOM/");
//
//        devProject.addJenkinsBuildProject(jenkinskCheckinBuild);
//
//        projects.add(devProject);
//    }

    public DevProject getCurrentProject() {
        return projects.iterator().next();
    }
    
    public Boolean getCheckNewJenkinsBuilds() {
        return checkNewJenkinsBuilds;
    }

    public void setCheckNewJenkinsBuilds(Boolean checkNewJenkinsBuilds) {
        this.checkNewJenkinsBuilds = checkNewJenkinsBuilds;
    }

    public String getLocalTempDir() {
        return localTempDir;
    }

    public void setLocalTempDir(String localTempDir) {
        this.localTempDir = localTempDir;
    }

    public Collection<VolvoEnv> getUserSelectedEnvsToCheck() {
        return userSelectedEnvsToCheck;
    }

    public void setUserSelectedEnvsToCheck(Collection<VolvoEnv> userSelectedEnvsToCheck) {
        this.userSelectedEnvsToCheck = userSelectedEnvsToCheck;
    }

    public String projectsToHTML() {        
        String toret = "";
        try {
            ObjectMapper mapper = new ObjectMapper();
            toret = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(projects);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return toret;
    }
    

}
