package edu.lawrence.friendfinder.interfaces.dtos;

// In-project includes [Entities]
import edu.lawrence.friendfinder.entities.Event;

// Java-level includes [Utility]
import java.util.UUID;

// Java-level includes [Time]
import java.time.Instant;

public class EventDTO {
	
	private UUID userid;
	
	private String name;
	
	private String description;

	private String location;
	
	private Instant startTime;
	
	private Instant endTime;
	
	private Integer numRegistered;
	
	public EventDTO() {
		name = "";
		description = "";
	}
	
	public EventDTO(Event core) {
		this();
		
		userid = core.getHost().getId();
		name = core.getName();
		description = core.getDescription();
		location = core.getLocation();
		startTime = core.getStartTime();
		endTime = core.getEndTime();
		
		numRegistered = 0;
		core.getRegistrations().forEach((r) -> numRegistered++);
	}

	public UUID getUserid() {
		return userid;
	}

	public void setUserid(UUID userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Instant getStartTime() {
		return startTime;
	}

	public void setStartTime(Instant startTime) {
		this.startTime = startTime;
	}

	public Instant getEndTime() {
		return endTime;
	}

	public void setEndTime(Instant endTime) {
		this.endTime = endTime;
	}

	public Integer getNumRegistered() {
		return numRegistered;
	}

	public void setNumRegistered(Integer numRegistered) {
		this.numRegistered = numRegistered;
	}
	
}