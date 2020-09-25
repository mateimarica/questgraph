package com.questgraph;

/**
Thrown when the access token is not valid.
*/
class BadResponseCodeException extends java.lang.Exception {
	public BadResponseCodeException() {}
	
	public BadResponseCodeException(String reason) {
		super(reason);
	}
}