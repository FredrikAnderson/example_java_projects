package com.fredrik.bookit.infra;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fredrik.bookit.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
	
//    Item findByName(String itemName);

//	@Query("select ip from ItemProperties ip where ip.name = :name")
//	ItemProperties findByName(String name);


//	@Query("select ip from ItemProperties ip where lower(ip.name) like lower(concat('%', :name, '%'))")
//	List<User> findBy(String name);
	
}