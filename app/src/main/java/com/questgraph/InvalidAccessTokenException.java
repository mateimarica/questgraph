package com.questgraph;

/**
Thrown when the access token is not valid.
*/
class InvalidAccessTokenException extends java.lang.Exception {
	public InvalidAccessTokenException() {}
	
	public InvalidAccessTokenException(String reason) {
		super(reason);
	}
}
