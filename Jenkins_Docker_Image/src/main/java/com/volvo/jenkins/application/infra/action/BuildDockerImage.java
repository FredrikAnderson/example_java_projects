package com.volvo.jenkins.application.infra.action;

import java.io.File;
import java.time.LocalDateTime;

import com.volvo.jenkins.application.infra.Docker;
import com.volvo.jenkins.application.infra.cmd.CommonUtil;
import com.volvo.jenkins.application.infra.cmd.GitUtil;

public final class BuildDockerImage {
    
    // private static Logger log = LoggerFactory.getLogger(DBUtils.class);
      
    private BuildDockerImage() {      
    }

	public static void main(String args[]) {
	    
        String dir = ".";
        File currentDir = new File(dir);
        
	    String version = null;
	    if (args[0] != null && !args[0].trim().isEmpty()) {
	        version = args[0];
        } else {
            version = System.getenv("VERSION");
        }

        System.out.println("Building docker image with version: " + version);

        CommonUtil.executeCmd("docker info");

        if (!CommonUtil.runningWindows()) {
            CommonUtil.executeCmd(currentDir, "chmod 777 cli-commands.txt");
            CommonUtil.executeCmd(currentDir, "chmod 777 start_testmanager.sh");
            CommonUtil.executeCmd(currentDir, "ls -la");
    	    
            CommonUtil.executeCmd(currentDir, "chmod 777 data/KOLA_variants_5000.txt");
            CommonUtil.executeCmd(currentDir, "chmod 777 data/baselines_software_configurator.csv");
            CommonUtil.executeCmd(currentDir, "chmod 777 data/KOLA_variants_20000.txt");
            CommonUtil.executeCmd(currentDir, "chmod 777 data/KOLA_variants_50000.txt");
            CommonUtil.executeCmd(currentDir, "chmod 777 data/KOLA_variants_full.txt");
            CommonUtil.executeCmd(currentDir, "chmod 777 data/baselines.txt");
            CommonUtil.executeCmd(currentDir, "ls -la data/");
    	    
            CommonUtil.executeCmd(currentDir, "chmod -R 777 target/wars");
    		CommonUtil.executeCmd(currentDir, "ls -la target/wars");
        } else {
            currentDir = new File(".");
        }
        
		Docker docker = new Docker();
		System.out.println(docker.toString());

//		docker build --build-arg=http_proxy=%HTTP_PROXY% --build-arg=home=%HOME% . --tag=fredrik/jenkins 

		String build = "docker build --build-arg=http_proxy=http://cloudpxgot1.srv.volvo.com:8080 --build-arg=home=C:\\Users\\yt52878\\  --tag=" + docker.getTag() + " .";
 		CommonUtil.executeCmd(currentDir, build);
		
		String tag1 = "docker tag " + docker.getTag() + " " + docker.getRepoName();
 		CommonUtil.executeCmd(tag1);

 	    String tag2 = "docker tag " + docker.getTag() + " " + docker.getRepoName() + ":" + version;
 	    CommonUtil.executeCmd(tag2);

        String login = "docker login --username=" + docker.getRepoUsername() + " --password=" + docker.getRepoPassword() + " " + docker.getRepo();
        CommonUtil.executeCmd(login);
        
        String push1 = "docker push " + docker.getRepoName();
        CommonUtil.executeCmd(push1);

        String push2 = "docker push " + docker.getRepoName() + ":" + version;
        CommonUtil.executeCmd(push2);
        
        notifyAboutBuild(version);

	}

 private static void notifyAboutBuild(String version) {

     String emailTo = System.getProperty("tm.email.to");     
     if (emailTo == null || emailTo.isEmpty()) {
         System.out.println("tm.email.to is empty, not sending email notification.");
         return;
     }

     String gitLog = GitUtil.getGitLog("remotes/origin/develop", 2, 40);
     
     StringBuilder buf = new StringBuilder();
     buf.append("<b>New TestManager version built:</b>");
     buf.append(version);
     buf.append("<br>");
     buf.append("<b>Time:</b>");
     buf.append(LocalDateTime.now());        
     buf.append("<br><br>");
     buf.append("<b>History from Git since last build:</b>");
     buf.append("<br>");
     buf.append(gitLog);
     buf.append("<br>");
     buf.append("Sent by build code.");
     buf.append("<br>");

//     System.out.println("Message to be emailed: " + buf.toString());    
     
//     Properties prop = new Properties();
//     prop.put("mail.smtp.auth", false);
//     prop.put("mail.smtp.starttls.enable", "false");
//     prop.put("mail.smtp.host", "mailgot.it.volvo.net");
//     prop.put("mail.smtp.port", "25");
//     
//     Session session = Session.getInstance(prop);
//             
//     try {
//         Message message = new MimeMessage(session);
//         message.setFrom(new InternetAddress("testmanager@volvo.com"));
//         message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));
//         message.setSubject("TestManager - Build notification: version: " + version);
//
//         String msg = buf.toString();
//            
//         MimeBodyPart mimeBodyPart = new MimeBodyPart();
//         mimeBodyPart.setContent(msg, "text/html");
//
//         Multipart multipart = new MimeMultipart();
//         multipart.addBodyPart(mimeBodyPart);
//         message.setContent(multipart);
//
//         Transport.send(message);            
//     } catch (Exception e) {
//         e.printStackTrace();
//     }       
 }

}
