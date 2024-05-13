package edu.lawrence.friendfinder.interfaces.dtos.out;

// In-project includes
import edu.lawrence.friendfinder.interfaces.dtos.ProfileDTO;

public class ProfileODTO extends ProfileDTO {
	
	private Integer id;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
}