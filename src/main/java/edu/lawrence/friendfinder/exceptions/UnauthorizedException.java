package edu.lawrence.friendfinder.exceptions;

public class UnauthorizedException extends FFAPIException {
	
	public static final long serialVersionUID = 1L;
	
	public UnauthorizedException() {
		super("User is not authorized to perform this action");
	}
	
	public UnauthorizedException(String... messages) {
		super(messages);
	}
	
	public UnauthorizedException(String firstMessage, String... followMessages) {
		super(firstMessage, followMessages);
	}
	
}