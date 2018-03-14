package com.volvo.drs.versiontool;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;
import com.offbytwo.jenkins.model.BuildChangeSetItem;
import com.offbytwo.jenkins.model.BuildResult;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.JobWithDetails;
import com.volvo.drs.versiontool.model.JenkinsBuildResult;
import com.volvo.drs.versiontool.model.JenkinsBuildStatus;
import com.volvo.drs.versiontool.model.JenkinsProject;


public class JenkinsManager {

    private static Logger log = LoggerFactory.getLogger(JenkinsManager.class);

    public static void buildJenkinsJobWithParameters(JenkinsProject jenkinsProject, Map<String, String> parameters) {
        try {
            JobWithDetails jenkinsBuildJob = getJenkinsBuildJob(jenkinsProject);
            jenkinsBuildJob.build(parameters);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static List<JenkinsBuildResult> getJenkinsBuilds(JenkinsProject jenkinsBuildProject) {
        JobWithDetails job = getJenkinsBuildJob(jenkinsBuildProject);
        List<JenkinsBuildResult> jenkinsResults = createJenkinsBuildResults(job);

        return jenkinsResults;
        
//        File statusFile = getStatusFile(jenkinsBuildProject.getName() + ".htm");
//
//        // DL status
//        Deployer.downloadFileFromURL(jenkinsBuildProject.getBuildProjectUrl(), statusFile, 
//                                     VersionTool.getVCNUserName(), VersionTool.getVCNPassword());
//        List<JenkinsBuildResult> proTOMJenkinsBuilds = getJenkinsBuildNrsFromJenkinsBuild(statusFile, jenkinsBuildProject.getName());
//        return proTOMJenkinsBuilds;
    }

    public static JenkinsBuildResult getLastSuccessfulBuild(JenkinsProject jenkinsBuildProject) {
        JobWithDetails job = getJenkinsBuildJob(jenkinsBuildProject);
        JenkinsBuildResult jenkinsResult = createJenkinsBuildResult(job.getLastSuccessfulBuild());

        return jenkinsResult;
    }

    public static List<JenkinsBuildResult> getSuccessfulBuildResults(JenkinsProject jenkinsBuildProject, int number) {
        JobWithDetails job = getJenkinsBuildJob(jenkinsBuildProject);
        List<JenkinsBuildResult> jbrs = new ArrayList<JenkinsBuildResult>();

        int addedGreenBuilds = 0;
        for (Build jenkBuild : job.getBuilds()) {
            try {
                if (jenkBuild.details().getResult().equals(BuildResult.SUCCESS) && addedGreenBuilds < number) {
                    JenkinsBuildResult jbr = createJenkinsBuildResult(jenkBuild);
                    jbrs.add(jbr);
                    addedGreenBuilds++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return jbrs;
    }

    private static JenkinsBuildResult createJenkinsBuildResult(Build build) {
        JenkinsBuildResult jenkinsBuildResult = new JenkinsBuildResult();

        try {
            BuildWithDetails details = build.details();

            jenkinsBuildResult = new JenkinsBuildResult(details.getFullDisplayName(), build.getNumber(), 
                                                        mapBuildResult(details.getResult()));

            Integer highestRevision = 0;
            for (BuildChangeSetItem chItem : details.getChangeSet().getItems()) {
                String commitId = chItem.getCommitId();
                int parseInt = Integer.parseInt(commitId);
                highestRevision = Math.max(highestRevision, parseInt);                
            }
            // Possible to add SVN rev?
            jenkinsBuildResult.setRevision(highestRevision);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jenkinsBuildResult;
    }

    private static List<JenkinsBuildResult> createJenkinsBuildResults(JobWithDetails job) {
        List<JenkinsBuildResult> jenkinsBuilds = new ArrayList<JenkinsBuildResult>();
        
        for (Build buildIter : job.getBuilds()) {
            JenkinsBuildResult jbr = createJenkinsBuildResult(buildIter);
            jenkinsBuilds.add(jbr);
        }
        return jenkinsBuilds;
    }


    private static JenkinsBuildStatus mapBuildResult(com.offbytwo.jenkins.model.BuildResult result) {
        if (result.equals(com.offbytwo.jenkins.model.BuildResult.SUCCESS)) {
            return JenkinsBuildStatus.GREEN;
        }
        if (result.equals(com.offbytwo.jenkins.model.BuildResult.UNSTABLE)) {
            return JenkinsBuildStatus.YELLOW;
        }
        return JenkinsBuildStatus.RED;
    }

    private static JobWithDetails getJenkinsBuildJob(JenkinsProject jenkinsProject) {
        JobWithDetails job = null;
        try {
            JenkinsServer jenkServer = 
                    new JenkinsServer(new URI("http://hudson.it.volvo.net"), 
                                      Settings.getVCNUserName(), Settings.getVCNPassword());

            job = jenkServer.getJob(jenkinsProject.getJenkinsName());
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return job;
    }
    
//    private static boolean checkDirAndCreate(File statusFile) {
//        try {
//            if (!statusFile.getParentFile().exists()) {
//                statusFile.getParentFile().mkdirs();
//            }
//        } catch (Exception ex) {
//            return false;
//        }
//        return true;
//    }

    private static String getLocalTempDir() {
        return Settings.getInstance().getLocalTempDir();
    }

    public static File getLocalDirToDeployTo() {
        File srcDir = new File(getLocalTempDir() + "ToDeploy");
        boolean dirOk = checkDirNCreate(srcDir);
        if (!dirOk) {
            srcDir = new File(getLocalTempDir());
            dirOk = checkDirNCreate(srcDir);
        }

        log.debug("Dir to put local deploys is: " + srcDir);
        return srcDir;
    }

    private static boolean checkDirNCreate(File srcDir) {
        log.info("Create deploy dir, " + srcDir);
        try {
            if (!srcDir.exists()) {
                srcDir.mkdirs();
            }
        } catch (Exception ex) {
            log.error("Unable to create deploy dir, " + srcDir);
            return false;
        }
        return true;
    }

}
