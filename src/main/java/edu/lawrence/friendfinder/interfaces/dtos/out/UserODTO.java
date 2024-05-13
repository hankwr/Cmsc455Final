package edu.lawrence.friendfinder.interfaces.dtos.out;

// Java-level includes [Utility]
import java.util.UUID;

import edu.lawrence.friendfinder.interfaces.dtos.UserDTO;

public class UserODTO extends UserDTO{
	
	private UUID id;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
}