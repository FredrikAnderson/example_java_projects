package com.fredrik.bookit.app.config;

import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
//@EnableAsync
//@EnableScheduling
//@EnableTransactionManagement
//@EnableJpaRepositories(basePackages = "com.volvo.edb", repositoryImplementationPostfix = "Bean")
@ComponentScan("com.fredrik.bookit")
@ServletComponentScan("com.fredrik.bookit")
//@PropertySource({ "classpath:classpath:/application-${spring.profiles.active}.properties", "classpath:/application.properties" })
public class BookITConfig {
	
}