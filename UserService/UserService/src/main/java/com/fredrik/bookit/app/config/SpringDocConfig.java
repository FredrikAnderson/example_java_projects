package com.fredrik.bookit.app.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
//@OpenAPIDefinition
@OpenAPIDefinition(
		info = @Info(
				title = "REST API", 
				version = "v0.1", 
				description = "REST APIs for supporting widget/webclient requests", 
				contact = 
				@Contact(
						name = "Fredrik Andersson", 
						email = "fanderson75@gmail.com", 
						url = "https://www.tenni.se"
						)
				)
		)

public class SpringDocConfig {

//	@Bean 
//	public Info info() {
//		Info toret = new Info();
//		
//		toret.setTitle("WDS Tech Spike");
//		toret.setDescription("REST API for a tech spike");
//		toret.setVersion("0.1");
//		
//		return toret;		
//	}

//	@Bean
//	public Docket api() {
//		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
//				.apis(RequestHandlerSelectors.basePackage("com.volvo.wds.rest.resource")).paths(PathSelectors.any())
//				.build();
//	}
//
//	private ApiInfo apiInfo() {
//		return new ApiInfoBuilder().title("WDS API").description("This is the REST API of WDS spike.")
////                .termsOfServiceUrl("http://springfox.io")
//				.contact(new Contact("Fredrik Anderson", "", "fredrik.andersson.2@volvo.com"))
////                .license("Apache License Version 2.0")
////                .licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")
//				.version("0.1").build();
//
//	}

}