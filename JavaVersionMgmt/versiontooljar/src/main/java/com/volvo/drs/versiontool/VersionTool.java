package com.volvo.drs.versiontool;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.volvo.drs.versiontool.model.DevProject;
import com.volvo.drs.versiontool.model.JenkinsProject;
import com.volvo.drs.versiontool.ui.DeployFromJenkinsWorker;
import com.volvo.drs.versiontool.ui.VersionToolUI;

/**
 * Main class for Protomomer tool.
 */
public class VersionTool {

    private static Logger log = LoggerFactory.getLogger(VersionTool.class);

    enum EnvStatus {
        OK, NOT_OK, UNKNOWN
    };


    private static VersionTool myInstance = null;

//    private TrayIcon trayIcon;
    private VersionToolUI toolUI;

    private HttpStatusChecker envChecker = null;
    private Thread checkerThread = null;
    private String environmentStatus = new String();


    public static void main(String[] args) {
        new VersionTool();
    }

    public VersionTool() {
        init(true);
    }

    public VersionTool(boolean ui) {
        init(ui);
    }

    private void init(boolean ui) {
        myInstance = this;
        Settings.getInstance();

        if (ui) {
            toolUI = new VersionToolUI();
        }
    
        envChecker = new HttpStatusChecker();
        getEnvChecker().setProject(Settings.getInstance().getCurrentProject());
        checkerThread = new Thread(getEnvChecker());
        checkerThread.start();
    }

    public static VersionTool getInstance() {
        return myInstance;
    }

    public Thread getCheckerThread() {
        return checkerThread;
    }

    public void makeDeployFromJenkins() {
        // Little hack
        makeDeployFromJenkins(Settings.getInstance().getCurrentProject(), 
                              Settings.getInstance().getCurrentProject().getJenkinsBuildProjects().iterator().next());
    }

    public void makeDeployFromJenkinsURL() {
        JenkinsProject jenkinsBuildProj = new JenkinsProject();
        String jenkinsBuildUrl = getJenkinsBuildURL();
        if (jenkinsBuildUrl != null && !jenkinsBuildUrl.isEmpty()) {
            jenkinsBuildProj.setBuildProjectUrl(jenkinsBuildUrl);
            jenkinsBuildProj.setName(getBuildNameFromBuildURL(jenkinsBuildUrl));
            makeDeployFromJenkins(Settings.getInstance().getCurrentProject(), jenkinsBuildProj);
        }
    }

    private static String getBuildNameFromBuildURL(String jenkinsBuildUrl) {
        String[] parts = jenkinsBuildUrl.split("/");
        String lastPart = parts[parts.length - 1];
        return lastPart;
    }

    public void makeDeployFromJenkins(DevProject project, JenkinsProject jenkinsBuildProj) {
        final JDialog dialog = new JDialog();
        dialog.setLayout(new BorderLayout());
        final JLabel status = new JLabel();
        final JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);

        dialog.add(status, BorderLayout.PAGE_START);
        dialog.add(progressBar, BorderLayout.PAGE_END);

        dialog.setMinimumSize(new Dimension(360, 80));
        dialog.pack();
        dialog.setLocationRelativeTo(null);

        DeployFromJenkinsWorker deployFromJenkins = new DeployFromJenkinsWorker(project, jenkinsBuildProj);
        deployFromJenkins.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                if ("progress".equals(evt.getPropertyName())) {
                    Integer intVal = (Integer) evt.getNewValue();
                    // System.out.println("Progress: " + intVal);
                    progressBar.setValue(intVal);
                    if (intVal.equals(new Integer(100))) {
                        dialog.dispose();
                    }
                }
                if ("process".equals(evt.getPropertyName())) {
                    String strVal = (String) evt.getNewValue();
                    // System.out.println("Process: " + strVal);
                    status.setText(strVal);
                }
                if ("waitingUserInput".equals(evt.getPropertyName())) {
                    Boolean waitingUserInput = (Boolean) evt.getNewValue();
                    dialog.setVisible(!waitingUserInput);
                }
            }
        });
        deployFromJenkins.execute();
        dialog.setVisible(true);

    }

    // Obtain the image URL
    protected static Image createImage(String path, String description) {
        URL imageURL = VersionTool.class.getResource(path);

        // Image image = Toolkit.getDefaultToolkit().getImage(path);
        // return image;

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

    private static String getJenkinsBuildURL() {
        String chosenJenkinsUrl = JOptionPane.showInputDialog(null, "Choose Jenkins Build URL", "");
        return chosenJenkinsUrl;
    }

    public String getEnvironmentStatus() {
        return environmentStatus;
    }

    public void setEnvironmentStatus(String environmentStatus) {
        this.environmentStatus = environmentStatus;

        if (toolUI != null) {
            toolUI.setToolTip(environmentStatus);
        }
    }

    public void setEnvStatus(EnvStatus envStatus) {
        Image image = null;
        if (toolUI != null) {
            if (envStatus == EnvStatus.OK) {
                image = createImage("/images/bullet_green.png", "Home");
            }
            if (envStatus == EnvStatus.NOT_OK) {
                image = createImage("/images/bullet_red.png", "Home");
            }
            if (envStatus == EnvStatus.UNKNOWN) {
                image = createImage("/images/bullet_grey.png", "Home");
            }
    
            if (image != null) {
                toolUI.setImage(image);
            }
        }
    }

    public HttpStatusChecker getEnvChecker() {
        return envChecker;
    }

    public String testDeploy() {
        Iterator<JenkinsProject> iterator = Settings.getInstance().getCurrentProject().getJenkinsBuildProjects().iterator();
        iterator.next();
        JenkinsProject jenkinsProject = iterator.next();
        
        Map<String, String> map = new HashMap<String, String>();
        map.put("ENVIRONMENT", "dev");
        map.put("BUILD_VERSION", "4.3.1007");
        JenkinsManager.buildJenkinsJobWithParameters(jenkinsProject, map);
        return "OK";
    }

}
