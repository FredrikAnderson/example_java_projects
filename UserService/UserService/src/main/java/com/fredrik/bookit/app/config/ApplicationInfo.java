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
package com.fredrik.bookit.app.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.env.Environment;

import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for versions.
 */
@Configuration
@Named
@PropertySource({
	"classpath:git.properties",
	"classpath:META-INF/build-info.properties"
})
@Slf4j
public class ApplicationInfo implements ApplicationListener<ContextRefreshedEvent>, InfoContributor {

	private static ApplicationInfo myInstance;

	String applicationVersion = "";

	@Autowired
	private Environment env;
	
	public ApplicationInfo() {
		myInstance = this;
	}

	@PostConstruct
	public void init() {
		resolveVersion();
	}
	
	public static String getVersion() {
		return myInstance.getApplicationVersion();
	}
	
	private String resolveVersion() {
		String versionFormat = env.getProperty("version.format");
		String[] splitStrings = versionFormat.split(";");
		applicationVersion = "";

		for (String string : splitStrings) {
			applicationVersion += env.getProperty(string) + ".";
		}
		applicationVersion = applicationVersion.substring(0, applicationVersion.length() - 1);

		return applicationVersion;
	}

	public String getApplicationVersion() {
		return applicationVersion;
	}

	public void logVersionInfo() {
		log.info("Version of application is: " + applicationVersion);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		resolveVersion();
		saveVersionToFile();
 		logVersionInfo();		
	}

	@Override
    public void contribute(Info.Builder builder) {
        Map<String, String> versionInfo = new HashMap<>();
        versionInfo.put("name", env.getProperty("info.app.name"));
        versionInfo.put("description", env.getProperty("info.app.description"));
        versionInfo.put("version", applicationVersion);

        builder.withDetail("app", versionInfo);
    }
	
	private void saveVersionToFile() {
		 
		try {
			Files.write(Paths.get("version.txt"), applicationVersion.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
