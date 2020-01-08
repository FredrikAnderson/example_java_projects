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

/**
 * Utility class for Dates.
 */
//CHECKSTYLE:OFF: checkstyle:magicnumber
public class Docker {    
    
    // private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);
    
    private final String REPO      = "mavenqa.got.volvo.net:18444";
    private final String TAG       = "fredrik/jenkins-image";
    private final String REPO_NAME = REPO + "/" + TAG;
    
	private final String REPO_USERNAME = "cs-ws-s-docker";
	private final String REPO_PASSWORD = "N9K7b9J7";    

    public Docker() {        
    }

	public String getRepo() {
		return REPO;
	}
	
	public String getTag() {
		return TAG;
	}

	public String getRepoName() {
		return REPO_NAME;
	}

	public String getRepoUsername() {
		return REPO_USERNAME;
	}

	public String getRepoPassword() {
		return REPO_PASSWORD;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Docker repo:      " + REPO + "\n");
		buf.append("Docker tag:       " + TAG + "\n");
		buf.append("Docker repo name: " + REPO_NAME + "\n");
		
		return buf.toString();
	}
	
}
