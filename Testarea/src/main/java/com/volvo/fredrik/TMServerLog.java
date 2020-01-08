package com.volvo.fredrik;

public class TMServerLog {

    public static void main(String[] args) {
        String env = args[0];
        String logExtra = ""; 
        if (args.length > 1) {
            logExtra = args[1];
        }
        String podname = "";
        
        String tm_oc_project = "tcptestmanager-test";        
        if (env.equals("qa")) {
            tm_oc_project = "tcptestmanager-qa";            
        }
        if (env.equals("prod")) {
            tm_oc_project = "tcptestmanager-prod";            
        }
        info("Setting proper Openshift project");
        CommonUtil.executeCmd("oc project " + tm_oc_project);

        info("Listing pods in project");
        String str = CommonUtil.executeCmd("oc get po");
        
//        System.out.println("Got: " + str);
        
        String lines[] = str.split("\\r?\\n");
        
        for (String string2 : lines) {
//            System.out.println("Line: " + string2);
            if (string2.contains(env)) {
                podname = string2.substring(0, string2.indexOf(' '));
            }
        }        
        info("Found podname:" + podname + ". About to retrieve log from pod, might take some time.");
        
        
        String logFile = "server.log";
        if (!logExtra.trim().isEmpty()) {
            logFile = logFile + "." + logExtra;
        }
        
        str = CommonUtil.executeCmd("oc rsync " + podname + ":/home/jboss/files/jboss/logs/" + logFile + " .");
//        System.out.println("Out: " + str);
 
        info("Opening logfile in your editor");
        str = CommonUtil.executeCmd("editor.bat " + logFile);
        
    }

    
    private static void info(String string) {
        System.out.println(string);
    }

}
