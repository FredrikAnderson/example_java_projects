package com.fredrik.bookit.app.config;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator.Feature;
import com.google.common.collect.Lists;

public final class OpenAPIToJsonSupport {

	private OpenAPIToJsonSupport() {
	}

	public static void main(String[] args) {

		System.out.println("Exeuting: OpenAPIToJsonSupport");

		File file = new File(".");
		System.out.println("Path: " + file.getAbsolutePath());

		addJsonIdentityInfoToFile("./target/generated-sources/openapi/src/main/java/com/fredrik/bookit/web/rest/model/ItemDTO.java", "id");
		addJsonIdentityInfoToFile("./target/generated-sources/openapi/src/main/java/com/fredrik/bookit/web/rest/model/ProjectDTO.java", "id");
		
		List<String> asList = Arrays.asList(args);
		for (String string : asList) {
			System.out.println("Arg: " + string);
		}

		// Resolve input parameters
//		String env = resolveEnvironment();
//		String version = resolveVersion();
		String passwd = resolvePassword();

		// Print config
//		System.out.println("Env: " + env);
//		System.out.println("Version: " + version);

		resolveActions();

		// Test stuff 
		if (!Objects.isNull(System.getProperty("testing"))) {
			testing();
		}
		

	}

	private static void addJsonIdentityInfoToFile(String fileName, String identifier) {
	
		File file = new File(fileName);
		boolean exists = file.exists();
		
		System.out.println(fileName + ", exists: " + exists + ", " + file.getAbsoluteFile());
		
		try {
			List<String> toLines = Lists.newArrayList();
			List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
			
			boolean foundImport = false;
			for (String line : lines) {

				// Add imports
				if (!foundImport && line.contains("import")) {
					toLines.add("import com.fasterxml.jackson.annotation.JsonIdentityInfo;");
					toLines.add("import com.fasterxml.jackson.annotation.ObjectIdGenerators;");
					foundImport = true;
				}				
				// Add JsonIdentityInfo
				if (line.contains("public class")) {
					toLines.add("@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property=\"" + identifier + "\")");
				}
				toLines.add(line);
			}
			Files.write(file.toPath(), toLines, StandardCharsets.UTF_8);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void testing() {

		ObjectMapper mapper = new ObjectMapper(new YAMLFactory().enable(Feature.MINIMIZE_QUOTES));
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
		
//		NetworkPolicy networkPolicy = new NetworkPolicy("edb-dev", "pmd-backend-test", "TCP", 8888);
//		
//		try {
//			mapper.writeValue(new File("test.yaml"), networkPolicy);
//		
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
	}

	private static void resolveActions() {
		
		if (Objects.nonNull(System.getProperty("cicd"))) {
			System.setProperty("build-docker-image", "");
			System.setProperty("push-docker-image", "");
			System.setProperty("deploy-to-openshift-env", "");
		}
		if (Objects.nonNull(System.getProperty("docker"))) {
			System.setProperty("build-docker-image", "");
			System.setProperty("push-docker-image", "");
		}
		if (Objects.nonNull(System.getProperty("push-image"))) {
			System.setProperty("push-docker-image", "");
		}

		if (Objects.nonNull(System.getProperty("build-docker-image"))) {
		
		}

		if (Objects.nonNull(System.getProperty("create-env"))) {
			System.setProperty("create-openshift-env", "");
		}

		if (Objects.nonNull(System.getProperty("deploy"))) {
			System.setProperty("deploy-to-openshift-env", "");
		}
		if (!Objects.isNull(System.getProperty("deploy-to-openshift"))) {
			System.setProperty("deploy-to-openshift-env", "");
		}
		
		if (!Objects.isNull(System.getProperty("delete-env"))) {
			System.setProperty("delete-openshift-env", "");
		}

	}


	private static String resolvePassword() {
		String envPasswd = System.getenv("PASSWD");
		if (envPasswd != null) {
			return envPasswd;
		}
		String propPasswd = System.getProperty("passwd");		
		if (propPasswd != null) {
			return propPasswd;
		}		
		return null;
	}
}
