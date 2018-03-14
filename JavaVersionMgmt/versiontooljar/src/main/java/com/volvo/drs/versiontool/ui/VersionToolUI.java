package com.volvo.drs.versiontool.ui;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.volvo.drs.versiontool.JenkinsManager;
import com.volvo.drs.versiontool.Settings;
import com.volvo.drs.versiontool.VersionTool;
import com.volvo.drs.versiontool.deployment.Deployer;
import com.volvo.drs.versiontool.model.DeployDestination;
import com.volvo.drs.versiontool.model.DeploymentEarFile;
import com.volvo.drs.versiontool.model.DevProject;
import com.volvo.drs.versiontool.model.JenkinsBuildResult;
import com.volvo.drs.versiontool.model.JenkinsProject;
import com.volvo.drs.versiontool.model.VolvoEnv;

/**
 * Main class for Protomomer tool.
 */
public class VersionToolUI {

    private static Logger log = LoggerFactory.getLogger(VersionToolUI.class);

    enum EnvStatus {
        OK, NOT_OK, UNKNOWN
    };


    private static VersionToolUI myInstance = null;

    private TrayIcon trayIcon;

    public VersionToolUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error trying to set L&F");
        }

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    public static VersionToolUI getInstance() {
        return myInstance;
    }

    public void setImage(Image image) {
        trayIcon.setImage(image);
    }

    public void setToolTip(String tooltip) {
        trayIcon.setToolTip(tooltip);
    }

//    public static VersionToolUI getInstance() {
//        return myInstance;
//    }

    private void createAndShowGUI() {
        // Check the SystemTray support
        if (!SystemTray.isSupported()) {
            log.error("SystemTray is not supported");
            return;
        }
        PopupMenu popup = new PopupMenu();
        trayIcon = new TrayIcon(createImage("/images/bullet_grey.png", "tray icon"));
        final SystemTray tray = SystemTray.getSystemTray();

        // Create a popup menu components
        MenuItem aboutItem = new MenuItem("About");
        MenuItem settingsItem = new MenuItem("Settings");
        MenuItem deployerItem = new MenuItem("Make deploy, from dir");
        MenuItem deployFromURLItem = new MenuItem("Make deploy, from Jenkins URL");
        MenuItem deployFromJenkinsItem = new MenuItem("Make deploy, from Jenkins");
        // MenuItem deployFromRepoItem = new MenuItem("Make deploy, from Repo");
        MenuItem envsItem = new MenuItem("Env. status");
        MenuItem exitItem = new MenuItem("Exit");

        // Add components to popup menu
        popup.add(aboutItem);
        popup.add(settingsItem);
        popup.addSeparator();
        // popup.add(deployFromRepoItem);
        popup.add(deployFromJenkinsItem);
        popup.add(deployFromURLItem);
        popup.add(deployerItem);
        popup.addSeparator();
        popup.add(envsItem);
        popup.addSeparator();
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);
        trayIcon.setImageAutoSize(true);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            log.error("TrayIcon could not be added.");
            return;
        }

        trayIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // JOptionPane.showMessageDialog(null, "This dialog box is run from System Tray");
                JOptionPane.showMessageDialog(null, "Environment status: \n" + VersionTool.getInstance().getEnvironmentStatus());
            }
        });

        envsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                VersionTool.getInstance().getEnvChecker().checkStatusOnServers();
                JOptionPane.showMessageDialog(null, "Environment status: \n" + VersionTool.getInstance().getEnvironmentStatus());
            }
        });


        deployFromJenkinsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeDeployFromJenkins(Settings.getInstance().getCurrentProject(), 
                                      Settings.getInstance().getCurrentProject().getJenkinsBuildProjects().iterator().next());
            }
        });

        deployFromURLItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                makeDeployFromJenkinsURL();
            }
        });

        deployerItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                File srcDir = null;

                JFileChooser fileChooser = new JFileChooser("C:\\Temp\\");
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                int choosenDir = fileChooser.showDialog(null, "Select dir to deploy from");
                if (choosenDir == JFileChooser.APPROVE_OPTION) {
                    srcDir = fileChooser.getSelectedFile();
                }
                if (srcDir == null) {
                    return;
                }

                // Check source dir
                if (srcDir == null || !srcDir.exists()) {
                    JOptionPane.showMessageDialog(null, "Directory, " + srcDir + ", doesn't exists.", "Directory doesn't exist", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                DeployDestination deployDestination = getDeployDestination(Settings.getInstance().getCurrentProject(), null);

                if (deployDestination != null) {
                    Deployer deployer = new Deployer();
                    deployer.makeDeploy(srcDir, Settings.getVCNUserName(), Settings.getVCNPassword(), deployDestination);
                    JOptionPane.showMessageDialog(null, "Deploy to, " + deployDestination.getDestinationAsString() + " finished.");
                }
            }
        });

        aboutItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "Tool for ProTOM project.\nCreated by Fredrik Andersson.");
            }
        });
        settingsItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SettingsDialog();
            }
        });

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                tray.remove(trayIcon);
                System.exit(0);
            }
        });
    }

    class DeployFromJenkins extends SwingWorker<Void, String> {
        private DeployDestination deployDestination = null;
        private DevProject project = null;
        private JenkinsProject jenkinsBuildProj = null;

        public DeployFromJenkins(DevProject project, JenkinsProject jenkinsBuildProj) {
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

            deployDestination = getDeployDestination(project, getPropertyChangeSupport());
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
                String artifactFromJenkinsBuild = getArtifactFromJenkinsBuild(localServerFile);
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
                artifactFromJenkinsBuild = getArtifactFromJenkinsBuild(localDcFile);
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

        DeployFromJenkins deployFromJenkins = new DeployFromJenkins(project, jenkinsBuildProj);
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

    public DeployDestination getDeployDestination(DevProject proj, PropertyChangeSupport propertyChangeSupport) {
        
//        Collection<String> first = proj.getWasNames();
        //TOOD Fix this!
        Collection<String> first = Arrays.asList("");

        if (propertyChangeSupport != null) {
            propertyChangeSupport.firePropertyChange("waitingUserInput", null, new Boolean(true));
        }
        int showDestOptionDialog = JOptionPane.showOptionDialog(null, "Choose destination", "Choose destination", JOptionPane.OK_CANCEL_OPTION,
                                                                JOptionPane.DEFAULT_OPTION, null, first.toArray(), null);
        if (showDestOptionDialog == -1) {
            return null;
        }
        if (propertyChangeSupport != null) {
            propertyChangeSupport.firePropertyChange("waitingUserInput", null, new Boolean(false));
        }
        log.debug("DestOption: " + showDestOptionDialog);

        VolvoEnv chosenEnv = (VolvoEnv) proj.getVolvoEnvs().toArray()[showDestOptionDialog];
        String chosenWebDavUrl = JOptionPane.showInputDialog(null, "Choose WebDav destination address", chosenEnv.getDeployArea());

        boolean autoDirExists = false;
        if (chosenWebDavUrl != null) {
            try {
                Sardine sardine = SardineFactory.begin(Settings.getVCNUserName(), Settings.getVCNPassword());
                autoDirExists = sardine.exists(chosenWebDavUrl + "/" + "auto");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        if (!autoDirExists) {
            JOptionPane.showMessageDialog(null, "Deployment required directory, auto, doesn't exists in destination area.", "Directory doesn't exist",
                                          JOptionPane.ERROR_MESSAGE);
            return null;
        }

        return new DeployDestination(chosenEnv.getDeployArea());
        // }
    }

    // Obtain the image URL
    protected static Image createImage(String path, String description) {
        URL imageURL = VersionToolUI.class.getResource(path);

        // Image image = Toolkit.getDefaultToolkit().getImage(path);
        // return image;

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

    public static String getArtifactFromJenkinsBuild(File jenkinsArtifactFile) {
        String artifactString = "";
        try {
            InputStream fis = new FileInputStream(jenkinsArtifactFile);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis, Charset.forName("UTF-8")));
            String line;
            // String pattern = ".*Revision:/(\\d{4})";
            String pattern = "\\<td\\>\\<a href=\"(artifact.*.ear)\"";
            Pattern p = Pattern.compile(pattern);

            // System.out.println("Finding rev.");
            while ((line = br.readLine()) != null) {
                // Deal with the line
                // System.out.println("line: " + line);

                Matcher m = p.matcher(line);
                if (m.find()) {
                    artifactString = m.group(1);
                }
            }

            // Done with the file
            br.close();
            br = null;
            fis = null;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return artifactString;
    }

    private static String getJenkinsBuildURL() {
        String chosenJenkinsUrl = JOptionPane.showInputDialog(null, "Choose Jenkins Build URL", "");
        return chosenJenkinsUrl;
    }

    public static void main(String[] args) {
        setLookAndFeel();
        myInstance = new VersionToolUI();
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error trying to set L&F");
        }
    }


}
