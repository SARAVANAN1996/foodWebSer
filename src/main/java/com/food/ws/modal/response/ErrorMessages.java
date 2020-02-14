package com.food.ws.modal.response;

public enum ErrorMessages {
	
	MISSING_REQUIRED_FIELD("Missing Required Field"),
	RECORD_ALREADY_EXISTS("Record Already Exists"),
	NO_RECORD_FOUND("User Not Found");

	private String errorMessage;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	private ErrorMessages(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}

