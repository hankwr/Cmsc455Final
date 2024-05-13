package edu.lawrence.friendfinder.interfaces.dtos.out;

// In-project includes [DTOs]
import edu.lawrence.friendfinder.interfaces.dtos.UserDTO;

// Java-level includes [Utility]
import java.util.UUID;

public class UserODTO extends UserDTO{
	
	private UUID id;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}
	
}