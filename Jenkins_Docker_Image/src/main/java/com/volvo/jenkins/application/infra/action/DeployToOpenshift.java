package com.volvo.jenkins.application.infra.action;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.volvo.jenkins.application.infra.Openshift;
import com.volvo.jenkins.application.infra.AppConfig;
import com.volvo.jenkins.application.infra.cmd.CommonUtil;

public final class DeployToOpenshift {
    
//    private static Logger LOG = LoggerFactory.getLogger(DeployToOpenshift.class);
    
    private DeployToOpenshift() {
        
    }

	public static void main(String args[]) {

	    if (!CommonUtil.runningWindows()) {
	        CommonUtil.executeCmd("ls -la");
	    }
		
		String version = null;
		if (args[1] != null && !args[1].trim().isEmpty()) {
		    version = args[1];
		} else {
		    version = System.getenv("VERSION");
		}
        String passwd = null;		
        if (args[2] != null && !args[2].trim().isEmpty()) {
            passwd = args[2];
        } else {
            passwd = System.getenv("PASSWD");
        }

        if (args[0].equals("prod")) {
            String encodeToString = Base64.getEncoder().encodeToString(passwd.getBytes());
                        
            String encodedWantedPasswd = "dG00ZXZlcg==";            
            if (!encodeToString.equals(encodedWantedPasswd)) {
                System.out.println("You try to deploy to prod and need correct password to do so, please try again.");
                return;
            } else {
                System.out.println("Correct password to deploy to prod, fine. Moving on.");
            }
        }
		
        Openshift oc = new Openshift(args[0]);
        System.out.println(oc.toString());
        System.out.println(oc.getDocker().toString());
        
        oc.login();

		System.out.println("Deploying for staging env: " + args[0]);
		System.out.println("Deploying version: " + version);

		String ocImport = "oc import-image " + oc.getAppName() + ":" + version + " --from=" + oc.getDocker().getRepoName() + ":" + version + 
		        " --confirm --insecure=true";
 		CommonUtil.executeCmd(ocImport);
		
		int waitTimeSeconds = 70;
		System.out.println("Waiting for imagestream to update seconds; " + waitTimeSeconds);
		sleep(waitTimeSeconds);
		
		// Will pause rollouts or deploys
		String ocPauseRollout = "oc rollout pause dc/" + oc.getAppName();
		CommonUtil.executeCmd(ocPauseRollout);

        List<String> configCommandsForEnv = AppConfig.getInstance().getConfigCommandsForStage(oc.getStage());
        for (String cfgCmd : configCommandsForEnv) {
            CommonUtil.executeCmd(cfgCmd);
        }
		
        // Removes all triggers
        String ocRmTriggers = "oc set triggers dc/" + oc.getAppName() + " --containers=" + oc.getAppName() + " --remove-all";
        CommonUtil.executeCmd(ocRmTriggers);

		// Set config trigger for image
        String ocTriggerImage = "oc set triggers dc/" + oc.getAppName() + " --containers=" + oc.getAppName() + 
                " --from-image=" + oc.getAppName() + ":" + version;
        CommonUtil.executeCmd(ocTriggerImage);

		// Sets configchange trigger to auto
        String ocTriggerConfig = "oc set triggers dc/" + oc.getAppName() + " --containers=" + oc.getAppName() + " --from-config --auto";
        CommonUtil.executeCmd(ocTriggerConfig);

		// Resume rollouts for dc
        String ocRolloutResume = "oc rollout resume dc/" + oc.getAppName();
        CommonUtil.executeCmd(ocRolloutResume);

        waitTimeSeconds = 5;
        sleep(waitTimeSeconds);

        // Skipping this, only hanging the build in Jenkins for some time
        // 
//		String ocRollout = "oc rollout status --watch dc/" + oc.getAppName();
// 		CommonUtil.executeCmd(ocRollout);
		
		notifyAboutDeployment(args[0], version);
	}

	private static void notifyAboutDeployment(String env, String version) {

	    String emailTo = System.getProperty("tm.email.to");	    
	    if (emailTo == null || emailTo.isEmpty()) {
	        System.out.println("tm.email.to is empty, not sending email notification.");
	        return;
	    }
	    
	    StringBuilder buf = new StringBuilder();
        buf.append("<b>Time:</b>");
        buf.append(LocalDateTime.now());        
        buf.append("<br>");
        buf.append("<b>Environment:</b>");
        buf.append(env);
        buf.append("<br>");
	    buf.append("<b>Deployed version:</b>");
	    buf.append(version);
        buf.append("<br>");
        buf.append("<b>By user:</b>");
        buf.append(System.getProperty("user.name"));
        buf.append("<br>");
        buf.append("<b>Using computer:</b>");
        buf.append(getComputerName());
        buf.append("<br>");
        buf.append("<br><br>");
        buf.append("Sent by deployment code.");
        buf.append("<br>");

//	    System.out.println(buf.toString());    
	    
	    Properties prop = new Properties();
	    prop.put("mail.smtp.auth", false);
	    prop.put("mail.smtp.starttls.enable", "false");
	    prop.put("mail.smtp.host", "mailgot.it.volvo.net");
	    prop.put("mail.smtp.port", "25");
	    
//        Session session = Session.getInstance(prop);
//	    	    
//        try {
//            Message message = new MimeMessage(session);
//            message.setFrom(new InternetAddress("testmanager@volvo.com"));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
//            message.setSubject("TestManager - Deployment notification: " + env + " version: " + version);
//
//            String msg = buf.toString();
//               
//            MimeBodyPart mimeBodyPart = new MimeBodyPart();
//            mimeBodyPart.setContent(msg, "text/html");
//
//            Multipart multipart = new MimeMultipart();
//            multipart.addBodyPart(mimeBodyPart);
//            message.setContent(multipart);
//
//            Transport.send(message);            
//        } catch (Exception e) {
//            e.printStackTrace();
//        }	    
    }

	private static String getComputerName() {
	    Map<String, String> env = System.getenv();
	    if (env.containsKey("COMPUTERNAME"))
	        return env.get("COMPUTERNAME");
	    else if (env.containsKey("HOSTNAME"))
	        return env.get("HOSTNAME");
        else if (env.containsKey("NODE_NAME"))
            return env.get("NODE_NAME");
	    else
	        return "Unknown Computer";
	}
	
    private static void sleep(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (Exception e) {
				e.printStackTrace();
		}
	}	
}
