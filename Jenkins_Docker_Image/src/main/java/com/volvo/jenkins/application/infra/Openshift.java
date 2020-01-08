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
package com.volvo.jenkins.application.infra;

import com.volvo.jenkins.application.infra.cmd.CommonUtil;

/**
 * Utility class for Dates.
 */
//CHECKSTYLE:OFF: checkstyle:magicnumber
public class Openshift {    

    
    /**
     * Create secret for docker registry
     * 
     * oc secrets new-dockercfg nexus-docker --docker-server=mavenqa.got.volvo.net:18444 --docker-username=docker --docker-password=tcuser --docker-email=docker@volvo.com
     * 
     * oc secrets link default nexus-docker --for=pull
     */

    /**
     * Creating a secret for database
     * oc secret new-basicauth capacity-db-secret --username=u_gtmgrq02_00 --password=4MmzOosd
     * 
     * oc set volume dc/dev --add -t secret --secret-name=capacity-db-secret --mount-path=/home/jboss/secrets/db
     */
    
    /**
     * Adding a user VCN-id to a given Openshift role, like admin and view
     * oc adm policy add-role-to-user admin a263647
     * oc adm policy add-role-to-user view tin2984
     * 
     */
    
    private String PROJECT        		= "spx-dev";
    private String APPLICATION_NAME     = "";

	private String STAGE 				= "";

	private final String OC_USERNAME    = "yt52878";
	private final String OC_PASSWORD    = "IawVIT19";    

	Docker docker;
	
    public Openshift(String appname) {        
		docker = new Docker();

		APPLICATION_NAME 	= appname;
    }

    public Openshift(String appname, String stage) {        
		docker = new Docker();

		if (appname == null || appname.isEmpty()) {
			APPLICATION_NAME 	= stage;
			STAGE 			    = stage;
			
		} else {
			APPLICATION_NAME 	= appname;
			STAGE 				= stage;			
		}		
    }

	public Docker getDocker() {
		return docker;
	}

	public String getProject() {
		return PROJECT;
	}
	
	public String getAppName() {
		return APPLICATION_NAME;
	}

	public String getOCUsername() {
		return OC_USERNAME;
	}

	public String getOCPassword() {
		return OC_PASSWORD;
	}

	public String getStage() {
		return STAGE;
	}

    /**
     * Deployment of image and configuration
     * 
     * oc login --username=$OC_USERNAME --password=$OC_PASSWORD ocp-admin.got.volvo.net:8443 --insecure-skip-tls-verify
     *
     * oc project $OC_PROJECT
     */
	public void login() {
		
		String loginCmd = "oc login --username="+ getOCUsername() +" --password="+ getOCPassword() +" ocp-admin.got.volvo.net:8443 --insecure-skip-tls-verify";
		CommonUtil.executeCmd(loginCmd, "oc login --username=" + getOCUsername());

		String projectCmd = "oc project " + getProject();
		CommonUtil.executeCmd(projectCmd);
	}
	
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Openshift project: " + PROJECT + "\n");
		buf.append("Openshift app name:" + APPLICATION_NAME + "\n");
		buf.append("Stage:             " + STAGE + "\n");
		
		return buf.toString();
	}
	
}
