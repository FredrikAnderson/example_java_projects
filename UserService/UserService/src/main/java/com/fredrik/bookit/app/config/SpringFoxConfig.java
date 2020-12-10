package com.fredrik.bookit.app.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


/**
 * Config for swagger REST API spec/doc generation.
 * *
 */
@Configuration
//@EnableSwagger2
//@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig {
	
//	@Autowired
//	BuildProperties buildProperties;
	
//    @Bean
//    public Docket apiDocket() {
//        return new Docket(DocumentationType.SWAGGER_2)
//                .select()
//                .apis(RequestHandlerSelectors.basePackage("com.fredrik.bookit.web.controller"))
//                .paths(PathSelectors.any())
//                .build()
////                .securitySchemes(getSecuritySchemes())
////                .securityContexts(getSecurityContexts())
//                .apiInfo(getApiInfo());
//    }
//
////    private List<? extends SecurityScheme> getSecuritySchemes() {
////        return Lists.newArrayList(new BasicAuth("parts"));
////    }
//
////    private List<SecurityContext> getSecurityContexts() {
////        return Lists.newArrayList(SecurityContext.builder()
////                .securityReferences(defaultAuth())
////                .forPaths(PathSelectors.regex("/.*"))
////                .build());
////    }
//
////    List<SecurityReference> defaultAuth() {
////        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
////        AuthorizationScope[] authorizationScopes = { authorizationScope };
////        return Lists.newArrayList(new SecurityReference("parts", authorizationScopes));
////    }
//
//    private ApiInfo getApiInfo() {
//        return new ApiInfo("Book IT", "Book IT", "1.0", null,
//                           new Contact("Fredrik Anderson", null, "fredrik.andersson.2@volvo.com"), null, null, Collections.emptyList());       
//    }
//
//    @Bean
//    public SecurityConfiguration security() {
//        return SecurityConfigurationBuilder.builder()
//                .clientId("")
//                .clientSecret("")
//                .scopeSeparator(" ")
//                .useBasicAuthenticationWithAccessCodeGrant(true)
//                .build();
//    }
}
