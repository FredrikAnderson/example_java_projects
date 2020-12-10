package com.volvo.fredrik;

public class OSSupport {

    public static void main(String[] args) {

		String env = args[0];
        String podpattern = args[1];

    	String podName = getPodName(env, podpattern);
        
        
        
//        output = CommonUtil.executeCmd("oc rsync " + podname + ":/home/jboss/files/jboss/logs/" + logFile + " .");
////        System.out.println("Out: " + str);
// 
//        info("Opening logfile in your editor");
//        output = CommonUtil.executeCmd("editor.bat " + logFile);
        
    }


	private static String getPodName(String env, String podPattern) {
        String podname = "";
        
        String oc_project = "rdm-test";        
//        if (env.equals("qa")) {
//            oc_project = "ldm-prod";            
//        }
        if (env.equals("prod")) {
            oc_project = "ldm-prod";            
        }
        info("Setting proper Openshift project");
        CommonUtil.executeCmd("oc project " + oc_project);

        info("Listing pods in project");
        String output = CommonUtil.executeCmd("oc get pods");
        info(output);
        
        String lines[] = output.split("\\r?\\n");
        
        for (String str : lines) {
//            System.out.println("Line: " + string2);
            if (str.contains(podPattern)) {
                podname = str.substring(0, str.indexOf(' '));
            }
        }        
        info("Found podname: " + podname + ".");
		return podname;
	}

    
    private static void info(String string) {
        System.out.println(string);
    }

}
