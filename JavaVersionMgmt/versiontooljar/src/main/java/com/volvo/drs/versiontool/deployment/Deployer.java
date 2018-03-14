package com.volvo.drs.versiontool.deployment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.sardine.Sardine;
import com.github.sardine.SardineFactory;
import com.volvo.drs.versiontool.Base64;
import com.volvo.drs.versiontool.model.DeployDestination;
import com.volvo.drs.versiontool.model.EnvStatus;

/**
 * Deploy helper class for deploying stuff to WAS dropzone area.
 */
public class Deployer {

    private static Logger log = LoggerFactory.getLogger(Deployer.class);
    
    private static final String webIndex = "ProtomWeb/";

    // Interval in seconds
    // private Integer intervalCheck = new Integer(300); // 5 min

    // private static File localSwDeployDir = new File("C:\\Temp\\ToDeploy\\Rev_5238\\"); // One ear
    private static File localSwDeployDir = new File("C:\\Temp\\ToDeploy\\Rev_5239\\"); // Two ears

    private boolean runningWithoutWebDavModificationMode = false;

    public Deployer() {
    }

    public static void main(String[] args) {

        Deployer deployer = new Deployer();

        deployer.makeDeploy(localSwDeployDir, "apa", "bepa", new DeployDestination(new File("C:\\Temp\\DeployArea")));

        // File[] searchFilesIn = deployer.searchFilesIn(localSwDeployDir, ".ear");
        //
        // for (File file : searchFilesIn) {
        // System.out.println("File found: "+ file);
        // }

        // String zipFile = "C:\\Temp\\test.zip";
        // deployer.createZipFile(zipFile, searchFilesIn);
        // deployer.copyFiles(searchFilesIn, new File("C:\\temp\\DeployArea"));
        // deployer.copyFiles(searchFilesIn, new File("U:\\protom\\"));
    }

    public static void downloadFileFromURL(String url, File toFile, String user, String password) {
        log.info("Downloading: " + url + " as: " + user + " to: " + toFile.getAbsolutePath());
        try {
            URL website;
            // website = new
            // URL("http://hudson.it.volvo.net/job/ProtusTOM/3631/com.volvo.protom$ProtomEAR/artifact/com.volvo.protom/ProtomEAR/1.0-SNAPSHOT/ProtomEAR-1.0-SNAPSHOT.ear");
            website = new URL(url);
            HttpURLConnection http = (HttpURLConnection) website.openConnection();
            if (user != null) {
                http.setDoOutput(true);
                String encoding = Base64.encodeBytes(("" + user + ":" + password).getBytes());
                http.setRequestProperty("Authorization", "Basic " + encoding);
            }

            ReadableByteChannel rbc = Channels.newChannel(http.getInputStream());
            FileOutputStream fos = new FileOutputStream(toFile);
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();

            log.info("Downloaded: " + url + " to file: " + toFile);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    public void makeDeploy(File fromDir, String user, String pass, DeployDestination deployDest) {
        log.info("Deploy to: " + deployDest + " as: " + user);

        File[] searchFilesIn = searchFilesIn(fromDir, ".ear");

        for (File file : searchFilesIn) {
            log.debug("File found: " + file);
        }

        copyFiles(searchFilesIn, user, pass, deployDest);
        createDeployFileAndCopy(searchFilesIn, fromDir, user, pass, deployDest);
    }

    private void createDeployFileAndCopy(File[] searchFilesIn, File fromLocalDir, String user, String pass, DeployDestination deployDest) {
        File deployDesc = new File(fromLocalDir, "DEPLOYAPPLICATION.pkg");
        PrintWriter writer;
        try {
            writer = new PrintWriter(deployDesc);
            for (File file : searchFilesIn) {
                writer.println(file.getName());
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (deployDest.getDirectory() != null) {
            copyFile(deployDesc, new File(deployDest.getDirectory() + java.io.File.separator, deployDesc.getName()));
            copyFile(deployDesc, new File(deployDest.getDirectory() + java.io.File.separator + "auto", deployDesc.getName()));
        } else {
            uploadFileToWebDav(deployDesc, user, pass, deployDest.getWebDavUrl());

            // Copy .pkg descriptor from root => auto dir.
            String copyFrom = deployDest.getWebDavUrl() + deployDesc.getName();
            String copyTo = deployDest.getWebDavUrl() + "auto" + "/" + deployDesc.getName();
            log.debug("Copy .pkg descriptor from " + copyFrom + " to " + copyTo);
            if (runningWithoutWebDavModificationMode) {
                log.debug("Debug mode, no WebDav copy is done.");
            } else {
                try {
                    Sardine sardine = SardineFactory.begin(user, pass);
                    sardine.copy(deployDest.getWebDavUrl() + "/" + deployDesc.getName(), 
                                 deployDest.getWebDavUrl() + "/" + "auto" + "/" + deployDesc.getName());
    
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
        }
    }

    public File[] searchFilesIn(File dir, final String fileEnd) {
        // File dir = new File(dirName);

        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(fileEnd);
            }
        });
    }

    public void createZipFile(String zipFileName, File[] zipFileIncludedFiles) {
        try {
            // out put file
            ZipOutputStream outZipFile = new ZipOutputStream(new FileOutputStream(zipFileName));

            for (File file : zipFileIncludedFiles) {
                log.debug("File: " + file + ", name:" + file.getName());
                FileInputStream fileIn = new FileInputStream(file);

                // name the file inside the zip file
                outZipFile.putNextEntry(new ZipEntry(file.getName()));
                outZipFile.setMethod(ZipOutputStream.DEFLATED);

                // buffer size
                byte[] b = new byte[1024];
                int count;

                while ((count = fileIn.read(b)) > 0) {
                    // System.out.println();
                    outZipFile.write(b, 0, count);
                }
                fileIn.close();
            }
            outZipFile.close();

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        log.info("Created ZIP file: " + zipFileName);
    }

    public static void copyFile(File sourceFile, File destFile) {
        log.debug("copyFile: " + sourceFile + ", to: " + destFile);

        try {
            if (!destFile.exists()) {
                destFile.createNewFile();
            }

            FileChannel source = null;
            FileChannel destination = null;

            try {
                source = new FileInputStream(sourceFile).getChannel();
                destination = new FileOutputStream(destFile).getChannel();
                destination.transferFrom(source, 0, source.size());
            } finally {
                if (source != null) {
                    source.close();
                }
                if (destination != null) {
                    destination.close();
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        log.debug("Copied file: " + sourceFile.getAbsolutePath());
    }

    public void copyFiles(File[] sourceFiles, String user, String pass, DeployDestination destDir) {
        if (destDir.getDirectory() != null) {

            if (!destDir.getDirectory().isDirectory()) {
                log.debug("Dir: " + destDir + " not found.");
                return;
            }

            for (File file : sourceFiles) {
                File destFile = new File(destDir.getDirectory(), file.getName());
                copyFile(file, destFile);
            }
        } else {
            // WebDAV area
            for (File file : sourceFiles) {
                uploadFileToWebDav(file, user, pass, destDir.getWebDavUrl());
            }
        }

        log.info("Copied " + sourceFiles.length + " files.");
    }

    private void uploadFileToWebDav(File localFile, String user, String pass, String webDavUrl) {
        String webDavUrlDescFile = webDavUrl + localFile.getName();
        log.debug("Upload file: " + localFile.getAbsoluteFile() + " as: " + user + " to: " + webDavUrlDescFile);
        if (runningWithoutWebDavModificationMode) {
            log.debug("Debug mode, no WebDav copy is done.");
        } else {
            try {
                FileInputStream inFile = new FileInputStream(localFile);
                Sardine sardine = SardineFactory.begin(user, pass);
                if (sardine.exists(webDavUrlDescFile)) {
                    sardine.delete(webDavUrlDescFile);
                }
                sardine.put(webDavUrlDescFile, inFile);

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    public static EnvStatus getEnvStatusForURL(String baseUrl) {
        int statusCode = 0;
        EnvStatus toreturn = new EnvStatus();

        try {
            URL httpUrl = new URL(baseUrl + webIndex);
            log.debug("Checking URL: " + httpUrl);
            HttpURLConnection http = (HttpURLConnection) httpUrl.openConnection();

            statusCode = http.getResponseCode();
            toreturn.setStatusCode(statusCode);
            // System.out.println(statusCode);
            // System.out.println("Msg: " + http.getResponseMessage() + "content, " + http.getContent());

            // SVN Rev#:
            // ProTOM Server version: Unknown version
            String got = new String("SVN Rev#:5646 Build#");

            Pattern p = Pattern.compile(".*Rev#:(\\d{4}).*");
            Matcher mat = p.matcher(got);
            if (mat.matches()) {
                String version = mat.group(1);
                toreturn.setScmVersion(Integer.parseInt(version));
            }

            // System.out.println("Matches: "+matches + ", Version: "+version);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return toreturn;
    }

}