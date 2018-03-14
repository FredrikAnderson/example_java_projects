package com.volvo.drs.versiontool.model;

import java.util.List;

/**
 * Jenkins build result.
 */
public class JenkinsBuildResult {

    private String buildName = null;
    private Integer buildNr = null;
    private JenkinsBuildStatus buildStatus;
    private Integer revision = null;
    
    public JenkinsBuildResult() {
    }

    public JenkinsBuildResult(String buildName, String buildNrStr, String buildResultStr) {
        this(buildName, Integer.parseInt(buildNrStr), jenkinsStringToBuildResult(buildResultStr));
    }

    public JenkinsBuildResult(String buildName, Integer buildNr, JenkinsBuildStatus buildStatus) {
        this.setBuildName(buildName);
        this.setBuildNr(buildNr);
        this.setBuildStatus(buildStatus);
    }

    public JenkinsBuildStatus getBuildStatus() {
        return buildStatus;
    }

    public void setBuildStatus(JenkinsBuildStatus buildStatus) {
        this.buildStatus = buildStatus;
    }

    public Integer getBuildNr() {
        return buildNr;
    }

    public void setBuildNr(Integer buildNr) {
        this.buildNr = buildNr;
    }

    public Integer getRevision() {
        return revision;
    }
    
    public void setRevision(Integer revision) {
        this.revision = revision;
    }
    
    public String toString() {
        if (revision == null) {
            return buildNr + ", " + buildResultToString(buildStatus);
        } else {
            return buildNr + ", " + buildResultToString(buildStatus) + ", #" + revision;
        }
    }

    private static String buildResultToString(JenkinsBuildStatus buildStatus) {
        if (buildStatus == JenkinsBuildStatus.GREEN) {
            return "Success";
        } else {
            return "Failed";
        }
    }

    private static JenkinsBuildStatus jenkinsStringToBuildResult(String buildResultStr) {
//        System.out.println("buildResultStr = " + buildResultStr);
        if (buildResultStr.contains("Success")) {
            return JenkinsBuildStatus.GREEN;
        } else {
            return JenkinsBuildStatus.RED;
        }
    }
    
    public static JenkinsBuildResult getLatestSuccessfulBuild(List<JenkinsBuildResult> buildResults) {
        for (JenkinsBuildResult jenkinsBuildResult : buildResults) {
            if (jenkinsBuildResult.getBuildStatus().equals(JenkinsBuildStatus.GREEN)) {
                return jenkinsBuildResult;
            }
        }
        return null;
    }

    public String getBuildName() {
        return buildName;
    }

    public void setBuildName(String buildName) {
        this.buildName = buildName;
    }

}
