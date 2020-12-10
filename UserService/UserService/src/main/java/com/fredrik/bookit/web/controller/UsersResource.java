package com.fredrik.bookit.web.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fredrik.bookit.booking.app.UserService;
import com.fredrik.bookit.model.User;

import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class UsersResource { // implements UsersApi {

	@Inject
	private UserService userService;

	@GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        return Collections.singletonMap("name", principal.getAttribute("name"));
    }
	
	public ResponseEntity<User> addUser(@Valid User userDTO) {
		log.info("addUser: " + userDTO);
		User toret = null;
		
		toret = userService.save(userDTO);		
		return ResponseEntity.ok(toret);
	}

    public ResponseEntity<User> getLoggedInUser(
    		@ApiParam(value = "ID of user to return", required=true) @PathVariable("id") String id,
    		@ApiParam(value = "Password for the user wanting to login", required=true) @PathVariable("password") String password) {
		
		User toret = userService.findOne(id);
		
//		toret = userService.save(userDTO);		
		return ResponseEntity.ok(toret);		
	}

	public ResponseEntity<Void> deleteUser(String id) {
		log.info("deleteUser: " + id);

		userService.delete(id);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}

	public ResponseEntity<User> getUserById(String id) {
		log.info("getUserById: " + id);
		
		User userDTO = userService.findOne(id);
		
		return ResponseEntity.ok(userDTO);
	}

    public ResponseEntity<List<User>> getUsers(@Valid String name) {
		log.info("getUsers");
		
		List<User> dtos = null;
		if (Objects.nonNull(name) && !name.isEmpty()) {
			dtos = userService.findBy(name);
		} else {		
			dtos = userService.findAll();
		}
//		UserDTOList dtoList = new UserDTOList();
//		dtoList.setItems(dtos);
		
		return ResponseEntity.ok(dtos);
	}

	public ResponseEntity<Void> updateUser(String id, @Valid User userDTO) {
		log.info("updateItem: " + id);
//		ItemDTO toret = null;
		Void toret = null;

		userService.save(userDTO);		

		return ResponseEntity.ok(toret);
	}
	
}