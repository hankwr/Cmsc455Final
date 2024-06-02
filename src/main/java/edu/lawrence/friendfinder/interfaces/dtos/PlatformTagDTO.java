package edu.lawrence.friendfinder.interfaces.dtos;

// In-project includes [Entities]
import edu.lawrence.friendfinder.entities.PlatformTag;

public class PlatformTagDTO {
	
	private Integer profile;
	
	private String name;

	public PlatformTagDTO() {}
	
	public PlatformTagDTO(PlatformTag core) {
		this();
		
		profile = core.getProfile().getId();
		name = core.getName();
	}
	
	public Integer getProfile() {
		return profile;
	}

	public void setProfile(Integer profile) {
		this.profile = profile;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}