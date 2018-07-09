package com.avenuecode.exceptions;

public class GraphNotFoundException extends RuntimeException {
	
	private static final long serialVersionUID = 2082605832935434778L;

	public GraphNotFoundException(String id) {
		super("Graph not found with id: " + id);
	}
	
}
