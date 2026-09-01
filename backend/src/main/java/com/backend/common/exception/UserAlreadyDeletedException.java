package com.backend.common.exception;

public class UserAlreadyDeletedException extends RuntimeException {
	public UserAlreadyDeletedException(String message) {
		super(message);
	}
}
