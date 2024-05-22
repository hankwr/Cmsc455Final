package edu.lawrence.friendfinder.services;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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
        newE.setTimeZone(e.getTimeZone());
        newE.setStartTime(CTService.strToInst(e.getStartTime(), newE.getTimeZone()));
        newE.setEndTime(CTService.strToInst(e.getEndTime(), newE.getTimeZone()));
        // When an event is first created, there should not be any registrants
        eventRepository.save(newE);
		return newE.getId().toString();
	}

    public Event findById(Integer id) {
		return eventRepository.findById(id).get();
	}

    public List<Event> findFuture() {
		return eventRepository.findFuture(Instant.now());
	}
}