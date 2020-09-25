package com.questgraph;

/**
Thrown when the manual authorization token is not valid.
*/
class InvalidManualAuthTokenException extends java.lang.Exception {
	public InvalidManualAuthTokenException() {}
	
	public InvalidManualAuthTokenException(String reason) {
		super(reason);
	}
}