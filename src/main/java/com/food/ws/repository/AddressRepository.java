package com.food.ws.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.food.ws.entity.AddressEntity;
import com.food.ws.entity.UserEntity;


@Repository
public interface AddressRepository extends CrudRepository<AddressEntity,Long>{

	//naming is crucial here 
	//to find all address start with findall
	//to save start with save
	List<AddressEntity> findAllByUserDetails(UserEntity userEntity);

	AddressEntity findByAddressId(String addressId);
	

}
