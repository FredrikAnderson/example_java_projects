package com.volvo.jenkins.application.infra.action;


import com.volvo.jenkins.application.infra.Openshift;
import com.volvo.jenkins.application.infra.cmd.CommonUtil;

public final class InitiateOpenshiftProject {
    
    private InitiateOpenshiftProject() {
    }

	public static void main(String[] args) {

		Openshift oc = new Openshift(args[0]);

		System.out.println(oc.toString());
		System.out.println(oc.getDocker().toString());	
		System.out.println("Should initiate project: " + oc.getProject() + " for env: " + args[0]);
		
		oc.login();
	
		String createDockerPullSecretCmd = "oc secrets new-dockercfg nexus-docker --docker-server=mavenqa.got.volvo.net:18444 "+
		        "--docker-username=docker --docker-password=tcuser --docker-email=docker@volvo.com";
		CommonUtil.executeCmd(createDockerPullSecretCmd);

		String linkSecretCmd = "oc secrets link default nexus-docker --for=pull";
		CommonUtil.executeCmd(linkSecretCmd);
	}	
}
