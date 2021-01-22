package com.questgraph.exception;

/**
Thrown when the access token is not valid.
*/
public class BadResponseCodeException extends java.lang.Exception {
	public BadResponseCodeException() {}
	
	public BadResponseCodeException(String reason) {
		super(reason);
	}
}