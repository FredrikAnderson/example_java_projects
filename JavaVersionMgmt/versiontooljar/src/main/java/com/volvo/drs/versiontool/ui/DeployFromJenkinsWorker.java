package com.volvo.drs.versiontool.ui;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.volvo.drs.versiontool.JenkinsManager;
import com.volvo.drs.versiontool.Settings;
import com.volvo.drs.versiontool.deployment.Deployer;
import com.volvo.drs.versiontool.model.DeployDestination;
import com.volvo.drs.versiontool.model.DeploymentEarFile;
import com.volvo.drs.versiontool.model.DevProject;
import com.volvo.drs.versiontool.model.JenkinsBuildResult;
import com.volvo.drs.versiontool.model.JenkinsProject;

public class DeployFromJenkinsWorker extends SwingWorker<Void, String> {

    private static Logger log = LoggerFactory.getLogger(DeployFromJenkinsWorker.class);

    private DeployDestination deployDestination = null;
    private DevProject project = null;
    private JenkinsProject jenkinsBuildProj = null;

    public DeployFromJenkinsWorker(DevProject project, JenkinsProject jenkinsBuildProj) {
        this.project = project;
        this.jenkinsBuildProj = jenkinsBuildProj;
    }

    @Override
    public Void doInBackground() {
        Integer buildNr = null;
        File srcDir = JenkinsManager.getLocalDirToDeployTo();
        Iterator<DeploymentEarFile> iterator = project.getProjectEarFiles().iterator();
        File localServerFile = new File(srcDir, iterator.next().getDeploymentEarFileName());
        File localDcFile = new File(srcDir, iterator.next().getDeploymentEarFileName());

        setProgress(5);
        publish("Retrieving information about available Jenkins builds");
        List<JenkinsBuildResult> proTOMJenkinsBuilds = JenkinsManager.getJenkinsBuilds(jenkinsBuildProj);
        setProgress(25);
        publish("Retrieved available Jenkins builds");

        firePropertyChange("waitingUserInput", null, new Boolean(true));
        int showOptionDialog = JOptionPane.showOptionDialog(null, "Choose Jenkins build to deploy", "Choose Jenkins build", JOptionPane.OK_CANCEL_OPTION,
                                                            JOptionPane.DEFAULT_OPTION, null, proTOMJenkinsBuilds.toArray(), proTOMJenkinsBuilds.get(0));
        if (showOptionDialog == -1) {
            return null;
        }
        firePropertyChange("waitingUserInput", null, new Boolean(false));
        buildNr = proTOMJenkinsBuilds.get(showOptionDialog).getBuildNr();
        log.info("Choosed Jenkins build nr: " + buildNr + ".");

        deployDestination = VersionToolUI.getInstance().getDeployDestination(project, getPropertyChangeSupport());
        if (deployDestination != null) {
            setProgress(40);
            JenkinsProject choosenJenkinsBuild = jenkinsBuildProj;
            publish("Downloading deployment file for chosen build, " + localServerFile.getName());
            Iterator<DeploymentEarFile> earFileNames = project.getProjectEarFiles().iterator();

            // Deployer.downloadFileFromURL(choosenJenkinsBuild.getBuildProjectUrl()
            // + buildNr + earFileNames.next().getProjectInternalEarFileName(),
            // localServerFile, getVCNUserName(), getVCNPassword());

            String projectInternalEarFileName = earFileNames.next().getProjectInternalEarFileName();
            Deployer.downloadFileFromURL(choosenJenkinsBuild.getBuildProjectUrl() + buildNr + projectInternalEarFileName, localServerFile,
                                         Settings.getVCNUserName(), Settings.getVCNPassword());

            setProgress(45);
            String artifactFromJenkinsBuild = VersionToolUI.getArtifactFromJenkinsBuild(localServerFile);
            Deployer.downloadFileFromURL(choosenJenkinsBuild.getBuildProjectUrl() + buildNr + projectInternalEarFileName + "/" + artifactFromJenkinsBuild,
                                         localServerFile, Settings.getVCNUserName(), Settings.getVCNPassword());

            setProgress(50);
            publish("Downloading deployment file for chosen build, " + localDcFile.getName());
            // Deployer.downloadFileFromURL(choosenJenkinsBuild.getBuildProjectUrl()
            // + buildNr + earFileNames.next().getProjectInternalEarFileName(),
            // localDcFile, getVCNUserName(), getVCNPassword());
            projectInternalEarFileName = earFileNames.next().getProjectInternalEarFileName();
            Deployer.downloadFileFromURL(choosenJenkinsBuild.getBuildProjectUrl() + buildNr + projectInternalEarFileName, localDcFile,
                                         Settings.getVCNUserName(), Settings.getVCNPassword());

            setProgress(55);
            artifactFromJenkinsBuild = VersionToolUI.getArtifactFromJenkinsBuild(localDcFile);
            Deployer.downloadFileFromURL(choosenJenkinsBuild.getBuildProjectUrl() + buildNr + projectInternalEarFileName + "/" + artifactFromJenkinsBuild,
                                         localDcFile, Settings.getVCNUserName(), Settings.getVCNPassword());

            publish("Downloaded deployment files for chosen build.");
            setProgress(60);

            Deployer deployer = new Deployer();
            deployer.makeDeploy(srcDir, Settings.getVCNUserName(), Settings.getVCNPassword(), deployDestination);
        }
        setProgress(90);

        return null;
    }

    @Override
    protected void done() {
        try {
            get();
            if (deployDestination != null) {
                JOptionPane.showMessageDialog(null, "Deploy to, " + deployDestination.getDestinationAsString() + " finished.");
            }
        } catch (Exception ignore) {
            ignore.printStackTrace();
        } finally {
            setProgress(100);
        }
    }

    protected void process(List<String> chunks) {
        for (String string : chunks) {
            firePropertyChange("process", null, string);
        }
    }
}
