package edu.lawrence.friendfinder.interfaces.dtos;

// In-project includes [Entities]
import edu.lawrence.friendfinder.entities.Registration;

// Java-level includes [Utility]
import java.util.UUID;

public class RegistrationDTO{
	
	private Integer eventid;
	
	private UUID userid;
	
	public RegistrationDTO() {}

	public RegistrationDTO(Registration core) {
		eventid = core.getId();
		userid = core.getUser().getId();
	}

	public Integer getEventid() {
		return eventid;
	}

	public void setEventid(Integer eventid) {
		this.eventid = eventid;
	}

	public UUID getUserid() {
		return userid;
	}

	public void setUserid(UUID userid) {
		this.userid = userid;
	}
	
}