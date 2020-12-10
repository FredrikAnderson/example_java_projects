package com.fredrik.bookit.booking.app;

import java.util.List;

import com.fredrik.bookit.model.User;

public interface UserService {

	List<User> findAll();

	List<User> findBy(String name);

    User findOne(String id);

    User findByName(String name);

	User save(User user);

	void delete(String id);

	
}