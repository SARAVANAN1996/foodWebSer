package com.food.ws.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.food.ws.shared.dto.UserDto;

public interface UserService extends UserDetailsService {
	
	UserDto newUser(UserDto user);

	UserDto getUser(String userName);

	UserDto getUserById(String id);
	
	UserDto updateUser(String id, UserDto user);

	void deleteUser(String id);

	List<UserDto> getUsers(int page, int limit);
	
//	UserDto checkUser(UserDto user);

}
