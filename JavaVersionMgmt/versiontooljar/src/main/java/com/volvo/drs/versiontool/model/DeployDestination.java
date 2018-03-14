package com.volvo.drs.versiontool.model;

import java.io.File;

/**
 * Deployment destination information container.
 */
public class DeployDestination {

    private String webDavUrl = new String();
    
    private File directory = null;
    
    public DeployDestination(String webDavUrl) {
        this.setWebDavUrl(webDavUrl);
    }

    public DeployDestination(File deployDir) {
        this.setDirectory(deployDir);
    }

    public String getWebDavUrl() {
        return webDavUrl;
    }

    public void setWebDavUrl(String webDavUrl) {
        this.webDavUrl = webDavUrl;
    }

    public File getDirectory() {
        return directory;
    }

    public void setDirectory(File directory) {
        this.directory = directory;
    }
    
    public String getDestinationAsString() {
        if (directory != null) {
            return directory.getAbsolutePath();
        } else {
            return webDavUrl;
        }
    }

    @Override
    public String toString() {
        return "DeployDestination [webDavUrl=" + webDavUrl + ", directory=" + directory + "]";
    }
    
}
