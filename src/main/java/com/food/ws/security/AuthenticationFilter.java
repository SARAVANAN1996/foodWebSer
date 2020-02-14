package com.food.ws.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.food.ws.SpringApplicationContext;
import com.food.ws.modal.request.LoginRequest;
import com.food.ws.service.UserService;
import com.food.ws.shared.dto.UserDto;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	
	private final AuthenticationManager authenticationManager;

	public AuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
		
		try {
			
			//read the email and password using LoginRequest
			LoginRequest creds = new ObjectMapper().readValue(req.getInputStream(),LoginRequest.class);
			
			//authenticate user by comparing the loadUserByUsername with the input
			return authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(creds.getEmail(),creds.getPassword(), new ArrayList<>()));
			
			//if authtication is successfully ,then successfullAuthentication will be called else it won't be called
			
		}catch(IOException e){
			throw new RuntimeException(e);
		}		
	}
	
	@Override
	public void successfulAuthentication(HttpServletRequest req, 
			                             HttpServletResponse res, FilterChain chain, Authentication auth) 
			                            		 throws IOException, ServletException{
		String userName = ((User) auth.getPrincipal()).getUsername();
		
		String token = Jwts.builder()
				.setSubject(userName)
				.setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
				.compact();
		
		UserService userService = (UserService) SpringApplicationContext.getBean("userServiceImpl");
		UserDto userDto = userService.getUser(userName);
		
		//client who receives this response will need to extract these webtoken and store
		//for any request the token must be included in the header of the request otherwise the request will not be authorized
		res.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);
		res.addHeader("UserID", userDto.getUserId());
		
	}
}
