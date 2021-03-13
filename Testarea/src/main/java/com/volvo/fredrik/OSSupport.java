package com.volvo.fredrik;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class OSSupport {

    public static void main(String[] args) {

		String arg1 = args[0];
        String arg2 = (args.length > 1 ? args[1] : "");
        String arg3 = (args.length > 2 ? args[2] : "");

        if (arg1.equals("pods")) {        
        	String podName = getPodName(arg2, arg3);
        }

        if (arg1.equals("portfw_pg")) {
        	String name = "postgres";
        	String podName = getPodName(arg2, name);

        	setupPortForward(podName, 15432, 5432);        	
        }

        if (arg1.equals("cleanis")) {
        	String is = arg2;
        	int daysBack = Integer.parseInt(arg3);
        	
        	String isOut = CommonUtil.executeCmd("oc describe is " + is);
        	
        	List<OsTag> tags = resolveTags(is, isOut);

        	LocalDate beforeDate = LocalDate.now().minusDays(daysBack);
			cleanTagsOlderThan(tags, beforeDate);
        	        	
        }

        
//        output = CommonUtil.executeCmd("oc rsync " + podname + ":/home/jboss/files/jboss/logs/" + logFile + " .");
////        System.out.println("Out: " + str);
// 
//        info("Opening logfile in your editor");
//        output = CommonUtil.executeCmd("editor.bat " + logFile);
        
    }


	private static void cleanTagsOlderThan(List<OsTag> tags, LocalDate beforeDate) {

		for (OsTag osTag : tags) {			
			if (osTag.getCreated().isBefore(beforeDate)) {
				
				System.out.println("Should be cleaned: " + osTag.toString());
				
				CommonUtil.executeCmd("oc tag " + osTag.getIs() + ":" + osTag.getName() + " -d");
			}			
		}
		
	}


	private static List<OsTag> resolveTags(String isName, String isOut) {
		List<OsTag> toret = new ArrayList<>();
		
		String[] strings = isOut.split("\\n");
		
		boolean readHeader = false;
		OsTag tag = null;
		for (String str : strings) {
//			System.out.println("Now: " + str);
			
			if (!readHeader && str.trim().isBlank()) {
				readHeader = true;
			}
			
			if (readHeader) {
				// Reading a tag
				if (tag != null) {
//					System.out.println("In tag: " + str);
					if (Objects.isNull(tag.getCreated()) && str.contains(" ago")) {
//						System.out.println("Ago: " + str);
						LocalDate created = LocalDate.now();
						if (str.contains("hours ago")) {
							str = str.replace("hours ago", "");
							str = str.trim();							
						}
						if (str.contains("days ago")) {
							str = str.replace("days ago", "");
							str = str.trim();
							created = created.minusDays(Integer.parseInt(str));
						}
						if (str.contains("weeks ago")) {
							str = str.replace("weeks ago", "");
							str = str.trim();		
							created = created.minusWeeks(Integer.parseInt(str));
						}
						if (str.contains("months ago")) {
							str = str.replace("months ago", "");
							str = str.trim();							
							created = created.minusMonths(Integer.parseInt(str));
						}
						tag.setCreated(created);
					}
					if (str.trim().isBlank() && Objects.nonNull(tag.getCreated())) {
						// End of a tag
						toret.add(tag);
						tag = null;
					}
				}
				
				if (Objects.isNull(tag) && !str.substring(0, Math.min(2, str.length())).trim().isEmpty()) {
					// New tag
//					System.out.println("New tag: " + str);
					tag = new OsTag();
					tag.setIs(isName);
					tag.setName(str);
					
				}
			}			
		}
		
		return toret;
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

	private static void setupPortForward(String pod, Integer localPort, Integer podPort) {
        
        info("Listing pods in project");
        String output = CommonUtil.executeCmd("oc port-forward " + pod + " " + localPort + ":" + podPort, true);
        info(output);
        
//        String lines[] = output.split("\\r?\\n");
//        
//        for (String str : lines) {
////            System.out.println("Line: " + string2);
//            if (str.contains(podPattern)) {
//                podname = str.substring(0, str.indexOf(' '));
//            }
//        }        
	}

    
    private static void info(String string) {
        System.out.println(string);
    }

}
