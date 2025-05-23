package com.example.todolist.exception;

public class UnauthorizedTaskAccessException extends RuntimeException {
	public UnauthorizedTaskAccessException(String message) {
		super(message);
	}
}