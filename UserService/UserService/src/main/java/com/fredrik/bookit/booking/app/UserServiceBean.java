package com.fredrik.bookit.booking.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import org.mapstruct.factory.Mappers;

import com.fredrik.bookit.infra.UserRepository;
import com.fredrik.bookit.model.User;
import com.fredrik.bookit.model.mapper.UserMapper;

import lombok.extern.slf4j.Slf4j;

@Named
@Slf4j
public class UserServiceBean implements UserService {

	@Inject 
	UserRepository userRepo;

	UserMapper userMapper = Mappers.getMapper(UserMapper.class);

	@Override
	public User findOne(String id) {
		Optional<User> byId = userRepo.findById(id);
		
		return byId.get();
	}

	@Override
	public User findByName(String itemName) {
		
		User toret = null; // itemMapper.mapEntityToDTO(byName);
		return toret;
	}

	@Override
	public User save(User dto) {
		// DTO to entity
//		User entity = userMapper.mapDTOToEntity(dto);

//		// If itemProperties not containing id then lookup based on name
//		if (Objects.isNull(entity.getProperties().getId())) {
//			log.info("ItemPropertis should be looked up. By name: " + entity.getProperties().getName());
//			
//			ItemProperties itemProperties = itemRepo.findByName(entity.getProperties().getName());
//			if (Objects.nonNull(itemProperties)) {
//				entity.setProperties(itemProperties);
//			}			
//		}
		
		User toret = userRepo.save(dto);		
		
		return toret;
	}

	@Override
	public List<User> findAll() {
		
		List<User> findAll = userRepo.findAll();
		
		return findAll;
	}

	@Override
	public List<User> findBy(String itemName) {
		
//		List<ItemProperties> findAll = itemRepo.findBy(itemName);
//		List<Item> items = findAll.stream().map(ip -> new Item(ip)).collect(Collectors.toList());		
		List<User> dtos = null; // mapEntitiesToDtos(items);
		
		return dtos;
	}
	
	@Override
	public void delete(String id) {
		userRepo.deleteById(id);
	}
	
	private List<User> mapEntitiesToDtos(List<User> users) {
		List<User> toret = new ArrayList<>();
		
		for (User user : users) {
//			UserDTO UserDTO = userMapper.mapEntityToDTO(user);
//			
//			toret.add(UserDTO);
			toret.add(user);
		}
		
		return toret;
	}

}