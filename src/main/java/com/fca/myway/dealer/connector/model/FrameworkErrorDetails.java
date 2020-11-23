package com.fca.myway.dealer.connector.model;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter

@ToString
public class FrameworkErrorDetails {

	private String message;
	private HttpStatus statusCode;

	public FrameworkErrorDetails(String message) {
		this.message = message;
	}

	public FrameworkErrorDetails(String message, HttpStatus statusCode) {

		this.message = message;
		this.statusCode = statusCode;
	}
}
