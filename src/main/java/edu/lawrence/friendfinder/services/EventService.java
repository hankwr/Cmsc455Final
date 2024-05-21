package edu.lawrence.friendfinder.services;

import java.util.List;

// Spring-level includes [Class Annotations]
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.lawrence.friendfinder.entities.Event;
import edu.lawrence.friendfinder.entities.User;
import edu.lawrence.friendfinder.exceptions.DuplicateException;
import edu.lawrence.friendfinder.interfaces.dtos.EventDTO;
import edu.lawrence.friendfinder.repositories.EventRepository;


// Spring-level includes [Field Annotations]

@Service
public class EventService{
	@Autowired
	EventRepository eventRepository;
	
    // Register new event
	public String save(EventDTO e) throws DuplicateException {
        
		// I removed duplicate checking for now b/c I'm not sure how we could do that effectively
        // There are so many parameters that would constitute a different event if changed, and
        // there is not one variable that is unique to each event

		Event newE = new Event();
		newE.setName(e.getName());
        newE.setDescription(e.getDescription());
        newE.setLocation(e.getLocation());
        newE.setStartTime(e.getStartTime());
        newE.setEndTime(e.getEndTime());
        // When an event is first created, there should not be any registrants
        eventRepository.save(newE);
		return newE.getId().toString();
	}
}