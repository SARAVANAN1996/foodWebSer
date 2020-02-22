package com.food.ws.controller;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.food.ws.shared.dto.AddressDTO;
import com.food.ws.shared.dto.UserDto;
import com.food.ws.Exception.UserServiceException;
import com.food.ws.modal.request.UserRequest;
import com.food.ws.modal.response.AddressRest;
import com.food.ws.modal.response.ErrorMessages;
import com.food.ws.modal.response.OperationStatusModel;
import com.food.ws.modal.response.RequestOperationName;
import com.food.ws.modal.response.RequestOperationStatus;
import com.food.ws.modal.response.UserResponse;
import com.food.ws.service.AddressService;
import com.food.ws.service.UserService;

@RestController
@RequestMapping("users")
@CrossOrigin(origins="*")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired 
	AddressService addressService;
	
	
	
	@GetMapping(path="/{id}", produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserResponse getUser(@PathVariable String id) {
		
		UserResponse returnValue = new UserResponse();
		
		UserDto userDto = userService.getUserById(id);
		BeanUtils.copyProperties(userDto,returnValue);
		return returnValue;
	}
	
	
	
	@PostMapping(path="/{sign-up}", 
			consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE} )
	public UserResponse newUser(@RequestBody UserRequest usrReq) throws Exception{
		
		try {
		//if( usrReq.getUsername().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		if( usrReq.getUsername().isEmpty()) throw new NullPointerException("The Object is null");
		
		//UserDto usrDto = new UserDto();
		//BeanUtils.copyProperties(usrReq, usrDto);
		
		ModelMapper modelMapper = new ModelMapper();
		UserDto usrDto = modelMapper.map(usrReq, UserDto.class);
		
		UserDto createdUsr = userService.newUser(usrDto);
		
		UserResponse usrRes = modelMapper.map(createdUsr, UserResponse.class);
	//	BeanUtils.copyProperties(createdUsr,usrRes);
		
		return usrRes;
		
		}
		catch(Exception e) {
			System.out.println(e);
		}
		return null;
	}
	
	
	
	
	@PutMapping(path="/{id}", 
			consumes = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public UserResponse updateUser(@PathVariable String id, @RequestBody UserRequest userRequest) {
		
        UserResponse usrRes = new UserResponse();
		
		//if( usrReq.getUsername().isEmpty()) throw new UserServiceException(ErrorMessages.MISSING_REQUIRED_FIELD.getErrorMessage());
		if( userRequest.getUsername().isEmpty()) throw new NullPointerException("The Object is null");
		
		UserDto usrDto = new UserDto();
		BeanUtils.copyProperties(userRequest, usrDto);
		
		UserDto createdUsr = userService.updateUser(id, usrDto);
		BeanUtils.copyProperties(createdUsr,usrRes);
		
		return usrRes;
	}
	
	
	
	@DeleteMapping(path="/{id}",
			produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public OperationStatusModel deleteUsr(@PathVariable String id) {
		
		OperationStatusModel returnValue = new OperationStatusModel();
		
		userService.deleteUser(id);
		returnValue.setOperationStatus(RequestOperationStatus.SUCCESS.name());
		returnValue.setOperationName(RequestOperationName.DELETE.name());
		
		return returnValue;
	}
	
	@GetMapping("/sample")
	public String getSample() {
		return "Hello";
	}
	
	@GetMapping(produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public List<UserResponse> getUsers(@RequestParam(value="page", defaultValue="0") int page, 
			                           @RequestParam(value="limit", defaultValue="3") int limit){
		
		List<UserResponse> returnValue = new ArrayList<>();
		
		List<UserDto> usrDto = userService.getUsers(page,limit);
		
		for(UserDto userDto : usrDto) {
			UserResponse usrRes = new UserResponse();
			BeanUtils.copyProperties(userDto, usrRes);
			returnValue.add(usrRes);
		}
	 
		return returnValue;
	}
	
	@GetMapping(path="/{id}/addresses",
			produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public List<AddressRest> getUserAddresses(@PathVariable String id){
		
		List<AddressRest> returnValue = new ArrayList();
		
		List<AddressDTO> addressesDto = addressService.getaddresses(id);
		
		if(addressesDto != null && !addressesDto.isEmpty()) {
			
			java.lang.reflect.Type listType = new TypeToken<List<AddressRest>>() {}.getType();
			returnValue = new ModelMapper().map(addressesDto, listType);
		}
		
		return returnValue;
	}
	
	@GetMapping(path="/{id}/addresses/{addressId}",
			produces= {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public AddressRest getUserAddress(@PathVariable String addressId){
		
        ModelMapper modelMapper = new ModelMapper();
        
		AddressDTO addressesDto = addressService.getaddress(addressId); 
		
		return modelMapper.map(addressesDto, AddressRest.class );
	}
//	
//	@PostMapping("/users/login")
//	public UserResponse checkUser(@RequestBody UserRequest login) {
//		
//		UserResponse usrRes = new UserResponse();
//		
//		UserDto usrDto = new UserDto();
//		BeanUtils.copyProperties(login, usrDto);
//		
//		UserDto createdUsr = userService.checkUser(usrDto);
//		BeanUtils.copyProperties(createdUsr,usrRes);
//		
//		return  usrRes;
//	}
	
	

}
