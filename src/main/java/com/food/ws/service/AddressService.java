package com.food.ws.service;

import java.util.List;

import com.food.ws.shared.dto.AddressDTO;
import com.food.ws.shared.dto.UserDto;

public interface AddressService {
	
	List<AddressDTO> getaddresses(String userId);

	AddressDTO getaddress(String id);

}
