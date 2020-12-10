package com.fredrik.bookit;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.mapstruct.factory.Mappers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fredrik.bookit.booking.app.UserService;
import com.fredrik.bookit.model.User;
import com.fredrik.bookit.model.mapper.UserMapper;
import com.fredrik.bookit.web.servlet.AppInfoServlet;
import com.fredrik.bookit.web.servlet.UserStartServlet;

@SpringBootApplication
public class UserSrvBackend implements CommandLineRunner {

	private static final Logger log = LoggerFactory.getLogger(UserSrvBackend.class);

//	@Override
//	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
//		return application.sources(Application.class);
//	}

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(UserSrvBackend.class);
		
		app.run(args);			 
	}

	// Register Servlet
	@Bean
	public ServletRegistrationBean userStartServletBean() {
		ServletRegistrationBean bean = new ServletRegistrationBean(new UserStartServlet(), "/user");
		return bean;
	}

	@Bean
	public ServletRegistrationBean appInfoServletBean() {
		ServletRegistrationBean bean = new ServletRegistrationBean(new AppInfoServlet(), "/support");
		return bean;
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
//				registry.addMapping("/api/users").allowedOrigins("http://localhost:4200");
//				registry.addMapping("/**").allowedOrigins("http://localhost:4200");
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("GET", "POST","PUT", "DELETE");
			}
		};
	}
	
	@Override
	public void run(String... args) throws Exception {
		log.info("Command runner");

//		loadInitData();
		log.info("Command runner - done.");
	}

	
	@Inject
	UserService userService;

	UserMapper userMapper = Mappers.getMapper(UserMapper.class);

	
//	public boolean containsProjectName(final List<Project> list, final String name) {
//	    return list.stream().map(Project::getName).filter(name::equals).findFirst().isPresent();
//	}
	
	@Bean
	@Profile( { "junit", "localdev", "dev", "test" } )
	@Transactional
	public void loadInitData() {
		log.info("Loading init data.");

		
		User myself = new User();
		myself.setUserid("me");
		myself.setName("Fredrik Anderson");
		myself.setEmail("fanderson75@gmail.com");
		myself.setRole("admin");
		
//		UserDTO userDTO = userMapper.mapEntityToDTO(myself);
		User userDTO = userService.save(myself);

		User magnus = new User();
		magnus.setUserid("Magnus");
		magnus.setName("Magnus Jarkvist");
		magnus.setEmail("magnus@meproduction.se");
		magnus.setRole("admin");
		
//		userDTO = userMapper.mapEntityToDTO(magnus);
		userDTO = userService.save(magnus);

		
		User booker = new User();
		booker.setUserid("booker");
		booker.setName("Mr Booker");
		booker.setEmail("booker@gmail.com");
		booker.setRole("booker");
		
//		userDTO = userMapper.mapEntityToDTO(booker);
		userDTO = userService.save(booker);

		User user = new User();
		user.setUserid("User1");
		user.setName("User#1");
		user.setEmail("user1@gmail.com");
		user.setRole("booker");
		
//		userDTO = userMapper.mapEntityToDTO(user);
		userDTO = userService.save(user);

		user.setUserid("User2");
		user.setName("User#2");
		user.setEmail("user2@gmail.com");
		user.setRole("booker");
		
//		userDTO = userMapper.mapEntityToDTO(user);
		userDTO = userService.save(user);

		user.setUserid("User3");
		user.setName("User#3");
		user.setEmail("user3@gmail.com");
		user.setRole("booker");
		
//		userDTO = userMapper.mapEntityToDTO(user);
		userDTO = userService.save(user);

		log.info("Loading of data, done.");
	}

}
