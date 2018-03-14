package com.volvo.drs.versiontool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.builder.ResponseSpecBuilder;
import com.jayway.restassured.config.ObjectMapperConfig;
import com.jayway.restassured.config.RestAssuredConfig;
import com.jayway.restassured.mapper.factory.Jackson2ObjectMapperFactory;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import com.jayway.restassured.specification.ResponseSpecification;
import com.volvo.drs.versiontool.model.DevProject;
import com.volvo.drs.versiontool.model.EnvStatus;
import com.volvo.drs.versiontool.model.JenkinsBuildResult;
import com.volvo.drs.versiontool.model.JenkinsProject;
import com.volvo.drs.versiontool.model.RuntimeEnvironment;
import com.volvo.drs.versiontool.model.VolvoEnv;
import com.volvo.drs.versiontool.model.WASEnv;

/**
 * Status checker class.
 */
public class HttpStatusChecker implements Runnable {

    private static Logger log = LoggerFactory.getLogger(HttpStatusChecker.class);

    private List<WASEnv> wasEnvs = new ArrayList<WASEnv>();
    private List<VolvoEnv> volvoEnvs = new ArrayList<VolvoEnv>();
        
    private DevProject project = null;
    
    private JenkinsProject jenkinsBuildProject = null;
    
    // Interval in seconds
    private Integer intervalCheck = new Integer(20);   // 5 min
    
    private HashMap<String, EnvStatus> statuses = new HashMap<String, EnvStatus>();

    private String dateStatus = new String();
    private String jenkinsStatus = new String();
    private String envStatus = new String();
    private String postStatus = new String();
    
    public HttpStatusChecker() {
    }
    
    public void setProject(DevProject project) {
        this.project = project;
        jenkinsBuildProject = project.getJenkinsBuildProjects().iterator().next();
    }
    
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
        // Run once at startup
//        JenkinsBuildResult lastSuccessfulBuild = JenkinsManager.getLastSuccessfulBuild(jenkinsBuildProject);
        List<JenkinsBuildResult> lastSuccessfulBuild = JenkinsManager.getSuccessfulBuildResults(jenkinsBuildProject, 5);
        setJenkinsStatus(lastSuccessfulBuild);
        
        while (true) {
            checkStatusOnServers();
            if (Settings.getInstance().getCheckNewJenkinsBuilds()) {
                checkToDeployIfNewGreenJenkinsBuild();
            }
            VersionTool.getInstance().setEnvironmentStatus(getStatus());        
            
            try {
                Thread.sleep(1000 * getIntervalCheck());
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }        
    }

    public void checkStatusOnServers() {
        volvoEnvs.clear();
        Collection<VolvoEnv> userSelectedEnvsToCheck = Settings.getInstance().getUserSelectedEnvsToCheck();
        for (VolvoEnv volvoEnv : userSelectedEnvsToCheck) {
            volvoEnvs.add(volvoEnv);
        }

        boolean allOk = true;
        clearEnvStatus();
        for (VolvoEnv volvoEnv : volvoEnvs) {
            EnvStatus envStatus = getStatusForRestURL(volvoEnv.getVersionUrl());
            if (envStatus == null) {
                VersionTool.getInstance().setEnvStatus(VersionTool.EnvStatus.UNKNOWN);
                return;
            } else {
                updateStatusForEnvironment(volvoEnv, envStatus);
                
                if (envStatus.getStatusCode() != 200) {
                    allOk = false;
                }
            }
        }
        if (allOk) {
            VersionTool.getInstance().setEnvStatus(VersionTool.EnvStatus.OK);
            postStatus += "All environments OK.";
        } else {
            VersionTool.getInstance().setEnvStatus(VersionTool.EnvStatus.NOT_OK);                
        }
    }

    private void checkToDeployIfNewGreenJenkinsBuild() {
        JenkinsBuildResult lastSuccessfulBuild = JenkinsManager.getLastSuccessfulBuild(jenkinsBuildProject);
        setJenkinsStatus(lastSuccessfulBuild);
        VersionTool.getInstance().setEnvironmentStatus(getStatus());        
        
        String updateInfo = checkIfUpdateNeededForBuild(lastSuccessfulBuild);
        if (!updateInfo.isEmpty()) {
            int showOptionDialog = JOptionPane.showConfirmDialog(null, updateInfo + "\nDeploy with new available SW revision?", "Deploy with new available SW", 
                                                                JOptionPane.OK_CANCEL_OPTION, JOptionPane.DEFAULT_OPTION, null);
           if (showOptionDialog == JOptionPane.YES_OPTION) {
               VersionTool.getInstance().makeDeployFromJenkins();
           }
        }
    }

    private String checkIfUpdateNeededForBuild(JenkinsBuildResult jenkinsBuildResult) {
        String updateText = new String();
        for (WASEnv wasenv : wasEnvs) {
            if (wasenv.getName().startsWith("ST")) {
                Integer currentVersion = statuses.get(wasenv.getName()).getScmRevision();
                if (statuses.get(wasenv.getName()).getScmRevision() < jenkinsBuildResult.getRevision()) {
                    updateText += "Current version: " + wasenv.getName() + ", #" + currentVersion 
                            + ". Jenkins build " + jenkinsBuildResult.getBuildNr() 
                            + " done on #" + jenkinsBuildResult.getRevision() + "\n";
                }
            }            
        }    
        return updateText;
    }


    private String getStatus() {
        return dateStatus + jenkinsStatus + envStatus + postStatus;
    }

    private void clearEnvStatus() {
        envStatus = "";
        postStatus = "";
    }

    private void updateStatusDate() {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(date);
        dateStatus = formattedDate + "\n";
    }    

    private void appendEnvStatus(RuntimeEnvironment env, EnvStatus newEnvStatus) {
        log.debug("Got HTTP code: " + newEnvStatus.getStatusCode() + ", from: " + env.getName() + ", Version: " 
                + newEnvStatus.getVersion());
        updateStatusDate();
        
        envStatus += "" + env.getName() + ": " + newEnvStatus.getStatusCode() + ", Version: " 
            + newEnvStatus.getVersion() + ", #" + newEnvStatus.getScmRevision() + "\r\n";
    }

    private void setJenkinsStatus(JenkinsBuildResult jbr) {
        
        jenkinsStatus = "Jenkins: \r\n";
        jenkinsStatus += jbr.getBuildName() + ": " + jbr.getBuildStatus() + ". #" + jbr.getRevision() + "\r\n";
    }

    private void setJenkinsStatus(List<JenkinsBuildResult> jenkinsBuildResults) {
        
        jenkinsStatus = "Jenkins: \r\n";
        for (JenkinsBuildResult jbr : jenkinsBuildResults) {
            jenkinsStatus += jbr.getBuildName() + ": " + jbr.getBuildStatus() + ". #" + jbr.getRevision() + "\r\n";
        }
    }

    public void updateStatusForEnvironment(RuntimeEnvironment env, EnvStatus newEnvStatus) {
        appendEnvStatus(env, newEnvStatus);
        
        EnvStatus environmentStatus = statuses.get(env.getName());
        statuses.put(env.getName(), newEnvStatus);
        
        // Version is updated
        if (environmentStatus != null && !environmentStatus.getVersion().equals(newEnvStatus.getVersion())) {
            JOptionPane.showMessageDialog(null, "Version for " + env + ", changed from: " 
                    + environmentStatus.getVersion() + " to " + newEnvStatus.getVersion() + ".");
        }
    }
    
    public static EnvStatus getEnvStatusForURL(String baseUrl) {
		int statusCode = 0;
		EnvStatus toreturn = new EnvStatus();
		
		try {
//			URL httpUrl = new URL(baseUrl + webIndex);
            URL httpUrl = new URL(baseUrl);
			log.info("Checking URL: " + httpUrl);
			String encoding = Base64.encodeBytes("tom1:tom1".getBytes());
			HttpURLConnection http = (HttpURLConnection) httpUrl.openConnection();
            http.setDoOutput(true);
            http.setRequestProperty("Authorization", "Basic " + encoding);
	 			
			statusCode = http.getResponseCode();
			toreturn.setStatusCode(statusCode);
			// System.out.println(statusCode);
//			System.out.println("Msg: " + http.getResponseMessage() + "content, " + http.getContent());

			InputStream in = (InputStream) http.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String gotFromHttp, line = reader.readLine();
			gotFromHttp = line;
			while ((line = reader.readLine()) != null) {
			    gotFromHttp += line;
			}			
	        in.close();
	        log.debug("Ha: " + gotFromHttp.toString());
	        
			// SVN Rev#:			
//			String gotA = "ProTOM Server version: SVN Version:1.0-SNAPSHOT, SVN Branch:, SVN Rev#:6051, Build#:3376, Built on: Linux, Built by:ciadm";

			Pattern p = Pattern.compile(".*Rev#:(\\d{5}).*");
			Matcher mat = p.matcher(gotFromHttp.toString());
			boolean matches = mat.matches();
			if (matches) {
			    String version = mat.group(1);
			    toreturn.setScmVersion(Integer.parseInt(version));
			}
			
//			System.out.println("Matches: "+matches + ", Version: "+version);
		} catch (UnknownHostException ioe) {
		    return null;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
		return toreturn;
	}

    public static EnvStatus getStatusForRestURL(String baseUrl) {
        EnvStatus toreturn = null;
        
        
        // Make REST Assured aware of JAXB annotations
        RestAssured.config = RestAssuredConfig.config()
                                              .objectMapperConfig(new ObjectMapperConfig().jackson2ObjectMapperFactory(new Jackson2ObjectMapperFactory() {
                                                  @Override
                                                  public ObjectMapper create(Class aClass, String s) {
                                                      ObjectMapper objectMapper = new ObjectMapper();
                                                      JaxbAnnotationIntrospector introspector = new JaxbAnnotationIntrospector(objectMapper.getTypeFactory());

                                                      objectMapper.setAnnotationIntrospector(introspector);
                                                      return objectMapper;
                                                  }
                                              }));

        // Set authorization header
//        RestAssured.authentication = RestAssured.preemptive().basic("v00-1002", "tTNuWt5vI3+p26hIf7hqOQ==");

        // Get a user
        ResponseSpecification responseSpec = new ResponseSpecBuilder().build();       
        RequestSpecification requestSpec = new RequestSpecBuilder().build();

        Response userResponse = RestAssured.given(requestSpec, responseSpec).get(baseUrl);
        Map<String, String> filteredAttributes = userResponse.body().as(Map.class);
        
        log.debug(userResponse.body().asString());
        
        String versionString = (String) filteredAttributes.get("Implementation-Version");
//        Integer version = Integer.parseInt(versionString);
        String scmRevString = (String) filteredAttributes.get("Implementation-Scm-Revision");
        Integer scmRev = Integer.parseInt(scmRevString);
        toreturn = new EnvStatus(userResponse.getStatusCode() == 200, versionString, scmRev);
        toreturn.setStatusCode(userResponse.getStatusCode());
        
        return toreturn;
    }

    public Integer getIntervalCheck() {
        return intervalCheck;
    }

    public void setIntervalCheck(Integer intervalCheck) {
        this.intervalCheck = intervalCheck;

        VersionTool.getInstance().getCheckerThread().interrupt();
    }

//  public static void main(String args[]) {
//
//    for (String url : urls) {
//        int code = getResponseCodeForURL(url);
//        System.out.println("Got HTTP code: "+ code + ", from: "+ url);          
//    }
//  }

}