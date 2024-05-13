package edu.lawrence.friendfinder.interfaces.dtos;
/**
 * This is a dto that the client will send to the server
 * Lacks certain fields that are computed in the backend (eg. ID)
 */

public class UserDTO{
	
	private String username;
	
	private String password;
	
	public UserDTO() {}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}