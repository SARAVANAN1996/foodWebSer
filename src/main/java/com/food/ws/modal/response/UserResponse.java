package com.food.ws.modal.response;

import java.util.List;

public class UserResponse {
	
	private String userId;
	private String userName;
	private String email;
	private List<AddressRest> addresses;
	
	
	public String getUsername() {
		return userName;
	}
	public void setUsername(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<AddressRest> getAddresses() {
		return addresses;
	}
	public void setAddresses(List<AddressRest> addresses) {
		this.addresses = addresses;
	}


}
