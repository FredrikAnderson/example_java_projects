package com.volvo.jenkins.application.infra.cmd;

public class Probe implements DeploymentConfig {

    private String probe;
    
    private String getUrl;
    
    private int initialDelaySeconds;
    
    private int timeoutSeconds;
    
    private int periodSeconds;

    String cmd = "oc set probe deploymentconfigs %s --%s --get-url=%s " +
            "--initial-delay-seconds=%d --timeout-seconds=%d --period-seconds=%d";
    
    public Probe(String probe, String getUrl, int initDelaySec, int timeoutSec, int periodSec) {
        this.setProbe(probe);
        this.setGetUrl(getUrl);
        this.setInitialDelaySeconds(initDelaySec);
        this.setTimeoutSeconds(timeoutSec);
        this.setPeriodSeconds(periodSec);
    }

    public String getProbe() {
        return probe;
    }

    public void setProbe(String probe) {
        this.probe = probe;
    }

    public String getGetUrl() {
        return getUrl;
    }

    public void setGetUrl(String getUrl) {
        this.getUrl = getUrl;
    }

    public int getInitialDelaySeconds() {
        return initialDelaySeconds;
    }

    public void setInitialDelaySeconds(int initialDelaySeconds) {
        this.initialDelaySeconds = initialDelaySeconds;
    }

    public int getTimeoutSeconds() {
        return timeoutSeconds;
    }

    public void setTimeoutSeconds(int timeoutSeconds) {
        this.timeoutSeconds = timeoutSeconds;
    }

    public int getPeriodSeconds() {
        return periodSeconds;
    }

    public void setPeriodSeconds(int periodSeconds) {
        this.periodSeconds = periodSeconds;
    }

    @Override
    public String getCommand(String env) {
        return String.format(cmd, env, probe, getUrl, initialDelaySeconds, timeoutSeconds, periodSeconds);
    }

    @Override
    public String toString() {
        return "Probe [probe=" + probe + ", getUrl=" + getUrl + ", initialDelaySeconds=" + initialDelaySeconds + ", timeoutSeconds=" + timeoutSeconds
                + ", periodSeconds=" + periodSeconds + "]";
    }    
    
}
