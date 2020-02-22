package com.food.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.food.ws.shared.Utils;
import com.food.ws.shared.dto.AddressDTO;
import com.food.ws.shared.dto.UserDto;
import com.food.ws.Exception.UserServiceException;
import com.food.ws.entity.UserEntity;
import com.food.ws.modal.response.ErrorMessages;
import com.food.ws.repository.UserRepository;
import com.food.ws.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository usrRpr;
	
	@Autowired
	Utils utils;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Override
	public UserDto newUser(UserDto user) {
		
		if(usrRpr.findByEmail(user.getEmail()) !=null) throw new RuntimeException("Record already exits"); 
		
		for(int i=0; i<user.getAddresses().size();i++) {
			AddressDTO addressDto = user.getAddresses().get(i);
			addressDto.setUserDetails(user);
			addressDto.setAddressId(utils.generateAddressId(30));
			user.getAddresses().set(i, addressDto);
		}
		
		ModelMapper modelMapper = new ModelMapper();
		//UserEntity usrEntity = new UserEntity();
		//BeanUtils.copyProperties(user, usrEntity);
		UserEntity usrEntity = modelMapper.map(user, UserEntity.class);
		
		String publicId = utils.generateUserId(30);
		usrEntity.setUserId(publicId);
		usrEntity.setPassword(bCryptPasswordEncoder.encode(usrEntity.getPassword()));
		
		UserEntity saveEntity = usrRpr.save(usrEntity);
		
		//UserDto returnValue = new UserDto();
		//BeanUtils.copyProperties(saveEntity, returnValue);
		UserDto returnValue  = modelMapper.map(saveEntity, UserDto.class);
		
		return returnValue;
	}

//	@Override
//	public UserDto checkUser(UserDto user) {
//		
//		UserDto status = new UserDto();
//		
//		String userString = user.getUsername();
//		String password = user.getPassword();
//        
//		 List<UserEntity> returnvalue = usrRpr.validate(userString,password);
//		 
//		 if(!returnvalue.isEmpty())
//			  status.setStatus("UserExist");
//		 else
//		      status.setStatus("Invalid");
//		
//		return status;
//	}

	//find the user by the email id from db
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		UserEntity userEntity = usrRpr.findByEmail(email);
		
		if(userEntity == null ) throw new UsernameNotFoundException(email);
		
		return new User(userEntity.getEmail(),userEntity.getPassword(), new ArrayList<>());
	}

	@Override
	public UserDto getUser(String email) {
		
		UserEntity userEntity = usrRpr.findByEmail(email);
		
		if(userEntity == null )throw new UsernameNotFoundException(email);
		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(userEntity, returnValue);
		
  
		return returnValue;
	}

	@Override
	public UserDto getUserById(String userId) {
		
		
		UserDto returnValue = new UserDto();
		UserEntity userEntity = usrRpr.findByUserId(userId);
		
		if(userEntity == null) throw new UsernameNotFoundException("User with id: " +userId+ " not found");
		
		BeanUtils.copyProperties(userEntity, returnValue);
		
		return returnValue;
	}

	@Override
	public UserDto updateUser(String id, UserDto user) {
		
		UserDto returnValue = new UserDto();
		UserEntity userEntity = usrRpr.findByUserId(id);
		
		if(userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		userEntity.setUsername(user.getUsername());
		
		UserEntity updateEntity = usrRpr.save(userEntity);
		BeanUtils.copyProperties(userEntity, returnValue);
		
		return returnValue;
	}

	@Override
	public void deleteUser(String id) {
		
		UserEntity userEntity = usrRpr.findByUserId(id);
		
		if(userEntity == null) throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
		
		usrRpr.delete(userEntity);
		
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		
		List<UserDto> returnValue = new ArrayList<>();
		
		Pageable pageableRequest = PageRequest.of(page, limit);
		
		Page<UserEntity> usersPage = usrRpr.findAll(pageableRequest);
		List<UserEntity> users = usersPage.getContent();
		
		for(UserEntity usrEntity : users ) {
			UserDto usrDto = new UserDto();
			BeanUtils.copyProperties(usrEntity, usrDto);
			returnValue.add(usrDto);
		}
		
		return returnValue;
	}

}
