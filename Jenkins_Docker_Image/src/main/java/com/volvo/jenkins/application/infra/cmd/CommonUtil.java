/*
 * Copyright 2009 Volvo Information Technology AB 
 * 
 * Licensed under the Volvo IT Corporate Source License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 * 
 *      http://java.volvo.com/licenses/CORPORATE-SOURCE-LICENSE 
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */
package com.volvo.jenkins.application.infra.cmd;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Utility class for Dates.
 */
//CHECKSTYLE:OFF: checkstyle:magicnumber
public class CommonUtil {    
    
    // private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);
    
    private static final int NUM_59 = 59;
    private static final int NUM_11 = 11;
    private static final int FOREVER_DAY = 30;
    
    
    public CommonUtil() {    
    }

    public static void executeCmd(String cmd) {
        executeCmd(null, cmd, null);
    }

    public static void executeCmd(String cmd, String output) {
        executeCmd(null, cmd, output);
    }

    public static void executeCmd(File dir, String cmd) {
        executeCmd(dir, cmd, null);
    }

	public static void executeCmd(File dir, String cmd, String output) {
	    if (output == null) {
	        System.out.println("Executing: " + cmd);
	    } else {
            System.out.println("Executing: " + output);
	    }


	    boolean executeCmd = true;	    
	    if (executeCmd) {
            File adir = new File(".");
            System.out.println("Current dir: " + adir.getAbsolutePath() + adir.getPath());
    		
    		List<String> list = new ArrayList<String>(Arrays.asList(cmd.split(" ")));		
    		try {
    			ProcessBuilder pb = new ProcessBuilder(list);
    			if (dir != null) {
    			    pb.directory(dir);
    			}			
    			pb.inheritIO();
    			
    			Process proc = pb.start();
    			proc.waitFor();
    		
    		} catch (Exception e) {
    			e.printStackTrace();
    		}
	    }
        
		System.out.println();
	}

    public static boolean runningWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }

}
