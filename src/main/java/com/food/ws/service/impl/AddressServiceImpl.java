package com.food.ws.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.food.ws.entity.AddressEntity;
import com.food.ws.entity.UserEntity;
import com.food.ws.repository.AddressRepository;
import com.food.ws.repository.UserRepository;
import com.food.ws.service.AddressService;
import com.food.ws.shared.dto.AddressDTO;


@Service
public class AddressServiceImpl implements AddressService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	AddressRepository addressRepository;
	

	@Override
	public List<AddressDTO> getaddresses(String userId) {
		
		List<AddressDTO> returnDto = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();
		
		UserEntity userEntity = userRepository.findByUserId(userId);
		
	
		if(userEntity == null) {
			
			return returnDto;
		}
		
		Iterable<AddressEntity> addresses =  addressRepository.findAllByUserDetails(userEntity);
		
		for(AddressEntity address : addresses) {
			
			returnDto.add(modelMapper.map(address, AddressDTO.class ));
		}
		
		return returnDto;
	}


	@Override
	public AddressDTO getaddress(String addressId) {
		
		List<AddressDTO> returnDto = new ArrayList<>();
		ModelMapper modelMapper = new ModelMapper();
		
		AddressEntity addressEntity = addressRepository.findByAddressId(addressId);
		
		if(addressEntity==null) return null;
		
		return modelMapper.map(addressEntity, AddressDTO.class );
	}

}
