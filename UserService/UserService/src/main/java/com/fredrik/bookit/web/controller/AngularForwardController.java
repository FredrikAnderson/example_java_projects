package com.fredrik.bookit.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AngularForwardController {

//	@RequestMapping(value = "/{path:swagger}**")	
//    public String redirectToSwagger(@PathVariable String path) {
//		System.out.println("Rediret to Swagger: " + path);
//        // Forward to home page so that route is preserved.
//        return "path";
//    }

	// Working
//    @RequestMapping({ "/items", "/users", "/user/**", "/projects", "/project/**", "/projects-gantt", "/logout", "/home" })
	//    @RequestMapping(value = "/**/{[path:[^\\.]*}")
//    @RequestMapping({ "^(?=.*api)(?=.*backend)/**" })
//	@RequestMapping(value = "/{[^\\.]*}")
	
	
//	@RequestMapping(value = "/{path:^[backend]+}/**")	
//	@RequestMapping(value = "/!(swagger-ui)")	

	// Swagger-UI doesn't work. backend.html works and angular related URLs
//	@RequestMapping(value = "/{path:[^\\.]+}/**")	

//	@RequestMapping({ "/items", "/users", "/user/**", 
//		"/projects", "/project/**", "/projects-gantt", "/projects-book-item",
//		"/logout", "/home" })
//    public String redirectToAngular() {
//        // Forward to home page so that route is preserved.
//        return "forward:/index.html";
//    }
	

} 