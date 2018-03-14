package com.fredrik.hello;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EnvironmentController {

    @RequestMapping("/environment")
    public String environment() {
        StringBuilder buf = new StringBuilder();

		Map<String, String> env = System.getenv();
		for (String envName : env.keySet()) {
			// System.out.format("%s=%s%n", envName, env.get(envName));
            buf.append(String.format("%s=%s%n", envName, env.get(envName)));
		} 
		
        return buf.toString();
    }
}