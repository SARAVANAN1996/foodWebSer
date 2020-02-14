package com.food.ws.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import com.food.ws.entity.UserEntity;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	
	UserEntity findByEmail(String user);

	//the method should be meaningful must start with find
	UserEntity findByUserId(String userId);
	
//	@Query(value = "SELECT * FROM users WHERE username = :userName AND password = :passWord", nativeQuery = true)
//    List<UserEntity> validate(@Param("userName") String userName,@Param("passWord") String passWord);

}
