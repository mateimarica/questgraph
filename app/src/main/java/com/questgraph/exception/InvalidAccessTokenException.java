package com.questgraph.exception;

/**
Thrown when the access token is not valid.
*/
public class InvalidAccessTokenException extends java.lang.Exception {
	public InvalidAccessTokenException() {}
	
	public InvalidAccessTokenException(String reason) {
		super(reason);
	}
}