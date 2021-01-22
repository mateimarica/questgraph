package com.questgraph.exception;

/**
Thrown when the manual authorization token is not valid.
*/
public class InvalidManualAuthTokenException extends java.lang.Exception {
	public InvalidManualAuthTokenException() {}
	
	public InvalidManualAuthTokenException(String reason) {
		super(reason);
	}
}