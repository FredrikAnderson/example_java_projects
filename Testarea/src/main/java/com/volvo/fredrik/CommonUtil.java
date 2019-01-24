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
package com.volvo.fredrik;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;

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

    public static String executeCmd(String cmd) {
        return executeCmd(null, cmd, null);
    }

    public static String executeCmd(String cmd, String output) {
        return executeCmd(null, cmd, output);
    }

    public static String executeCmd(File dir, String cmd) {
        return executeCmd(dir, cmd, null);
    }

	public static String executeCmd(File dir, String cmd, String output) {
	    String toreturn = "";
	    
	    if (output == null) {
	        System.out.println("Executing: " + cmd);
	    } else {
            System.out.println("Executing: " + output);
	    }

        File adir = new File(".");
//        System.out.println("Current dir: " + adir.getAbsolutePath() + adir.getPath());

//        if (dir == null) {
//            System.out.println("Using dir: " + dir);                    
//        } else {
//            System.out.println("Using dir: " + dir.getAbsolutePath());        
//        }
		
		List<String> list = new ArrayList<String>(Arrays.asList(cmd.split(" ")));		
		try {
			ProcessBuilder pb = new ProcessBuilder(list);
			if (dir != null) {
			    pb.directory(dir);
			}
			
//			ProcessBuilder pb = new ProcessBuilder("ping jira.it.volvo.net");
//			pb.inheritIO();
			
			Process proc = pb.start();
			proc.waitFor();
			
			InputStream inputStream = proc.getInputStream();			
			StringWriter writer = new StringWriter();
			IOUtils.copy(inputStream, writer);
			toreturn = writer.toString();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
		
		return toreturn;
	}

    public static boolean runningWindows() {
        return System.getProperty("os.name").startsWith("Windows");
    }

}
