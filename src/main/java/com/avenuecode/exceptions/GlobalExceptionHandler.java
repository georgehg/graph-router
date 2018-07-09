package com.avenuecode.exceptions;

import java.util.AbstractMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value = GraphNotFoundException.class)
	protected ResponseEntity<AbstractMap.SimpleEntry<String, String>> handle(GraphNotFoundException exception) {
		AbstractMap.SimpleEntry<String, String> response =
				new AbstractMap.SimpleEntry<>("message", exception.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@ExceptionHandler
	public ResponseEntity<AbstractMap.SimpleEntry<String, String>> handle(Exception exception) {
		AbstractMap.SimpleEntry<String, String> response = new AbstractMap.SimpleEntry<>("message",
				"Unable to process this request.");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

}
