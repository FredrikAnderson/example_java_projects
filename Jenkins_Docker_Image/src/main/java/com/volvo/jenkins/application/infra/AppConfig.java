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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.volvo.jenkins.application.infra.cmd.CpuResources;
import com.volvo.jenkins.application.infra.cmd.DeploymentConfig;
import com.volvo.jenkins.application.infra.cmd.DeploymentConfigList;
import com.volvo.jenkins.application.infra.cmd.DeploymentStrategy;
import com.volvo.jenkins.application.infra.cmd.MemoryResources;

/**
 * Utility class for Dates.
 */
//CHECKSTYLE:OFF: checkstyle:magicnumber
public class AppConfig {    
    
    // private static final Logger LOGGER = LoggerFactory.getLogger(DateUtil.class);

    Map<String, DeploymentConfig> configs = new HashMap<String, DeploymentConfig>();
    Map<String, DeploymentConfig> overridden_cfgs = new HashMap<String, DeploymentConfig>();

    private static AppConfig myInstance = null;
    
    private AppConfig() {        
        
        MemoryResources memory = new MemoryResources(500, 2000);    // 500MB            max 3200 MB
        CpuResources cpu = new CpuResources(200, 1000);            // 1000 millicore   2000 millicore
        DeploymentStrategy depStrategy = new DeploymentStrategy("Recreate");
//        Probe readinessProbe = new Probe("readiness", "http://:8080/tm-uiservice/api/environment/ready", 200, 600, 30);
//        Probe livenessProbe = new Probe("liveness", "http://:8080/tm-uiservice/api/environment/health", 600, 60, 300);
//        EnvironmentVariable env1 = new EnvironmentVariable("ENABLE_ACCESS_LOG", "true");
//        EnvironmentVariable env2 = new EnvironmentVariable("JAVA_MAX_MEM_RATIO", "97");
//        EnvironmentVariable env3 = new EnvironmentVariable("SPRING_PROFILES_ACTIVE", "_stage_");
//        EnvironmentVariable env4 = new EnvironmentVariable("JAVA_OPTS_APPEND", 
//                                                           "-Dcom.volvo.jvs.env=_stage_ " + 
//                                                           "-Djboss.server.log.dir=/home/jboss/files/jboss/logs ");
//        Environment envVars = new Environment( env1, env2, env3 );
        
		// Default configuration
		// 
        configs.put("memory", memory);
        configs.put("cpu", cpu);
        configs.put("deploymentStrategy", depStrategy);
//        configs.put("livenessProbe", livenessProbe);
//        configs.put("readinessProbe", readinessProbe);
//        configs.put("environmentVars", envVars);

		
		
		// Overridden configs
        // Dev
        MemoryResources memoryDev = new MemoryResources(500, 2400);
        overridden_cfgs.put("memory-dev", memoryDev);
        CpuResources cpuDev = new CpuResources(100, 1000);
        overridden_cfgs.put("cpu-dev", cpuDev);
        
        // Dev2
        MemoryResources memoryDev2 = new MemoryResources(500, 2400);
        overridden_cfgs.put("memory-dev2", memoryDev2);
        CpuResources cpuDev2 = new CpuResources(100, 500);
        overridden_cfgs.put("cpu-dev2", cpuDev2);

    }
    
    public static AppConfig getInstance() {
        if (myInstance == null) {
            myInstance = new AppConfig();
        }
        return myInstance;
    }

    public List<String> getConfigCommandsForStage(String stage) {
        List<String> toret = new ArrayList<>();

        for (String config : configs.keySet()) {
            List<DeploymentConfig> depCfg = getConfigForStage(config, stage);
            for (DeploymentConfig deploymentConfig : depCfg) {                
                toret.add(deploymentConfig.getCommand(stage));
            }
        }        
        return toret;
    }
    
	private List<DeploymentConfig> getConfigForStage(String config, String stage) {	    
	    List<DeploymentConfig> toret = new ArrayList<DeploymentConfig>();
	    DeploymentConfig deploymentConfig      	= configs.get(config);
	    DeploymentConfig deploymentConfigStage  = overridden_cfgs.get(config + "-" + stage);
	    
	    // If override config exists then take it
	    if (deploymentConfigStage != null) {
	        deploymentConfig = deploymentConfigStage;
	    }
	    if (deploymentConfig instanceof DeploymentConfigList) {	        
	        DeploymentConfigList deploymentConfigList = (DeploymentConfigList) deploymentConfig;
	        toret.addAll(deploymentConfigList.getConfigList(stage));
	    } else {
	        toret.add(deploymentConfig);
	    }	    
        return toret;
    }

    public String toString() {
        
        for (String key : configs.keySet()) {
            System.out.println(configs.get(key).toString());                        
        }
        
        System.out.println();
        
	    return null;
	}
	
}
