package com.avenuecode.exceptions;

public class InvalidRouteException extends RuntimeException {

	private static final long serialVersionUID = -3396059759170464813L;

	public InvalidRouteException(String route) {
		super("Invalid Route: Route is repeated or have the same source and target node: " + route);
	}
	
}
