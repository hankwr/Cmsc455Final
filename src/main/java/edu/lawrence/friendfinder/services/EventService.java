package edu.lawrence.friendfinder.services;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

// Spring-level includes [Class Annotations]
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.lawrence.friendfinder.entities.Event;
import edu.lawrence.friendfinder.entities.Registration;
import edu.lawrence.friendfinder.exceptions.DuplicateException;
import edu.lawrence.friendfinder.exceptions.InvalidException;
import edu.lawrence.friendfinder.interfaces.dtos.EventDTO;
import edu.lawrence.friendfinder.interfaces.dtos.RegistrationDTO;
import edu.lawrence.friendfinder.repositories.EventRepository;
import edu.lawrence.friendfinder.repositories.RegistrationRepository;
import edu.lawrence.friendfinder.repositories.UserRepository;


// Spring-level includes [Field Annotations]

@Service
public class EventService{
	@Autowired
	EventRepository eventRepository;

    @Autowired
    RegistrationRepository registrationRepository;

    @Autowired
    UserRepository userRepository;
	
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

    public Event findById(Integer id) {
		return eventRepository.findById(id).get();
	}

    public List<Event> findFuture() {
		return eventRepository.findFuture(Instant.now());
	}

    public void saveRegistration(RegistrationDTO reg) throws DuplicateException, InvalidException {
        // Check if event exists
        Optional<Event> maybeEvent = eventRepository.findById(reg.getEventid());
        if(maybeEvent.isPresent())
			throw new InvalidException();
        Event event = maybeEvent.get();

        // Create Registration object
        Registration newReg = new Registration();
        newReg.setUser(userRepository.findById(reg.getUserid()).get());
        newReg.setEvent(event);

        // Check for duplicates in Registration table
        Optional<Registration> maybeRegistration = registrationRepository.checkForDuplicates(newReg.getEvent().getId(), newReg.getUser().getId());
		if(!maybeRegistration.isPresent())
			throw new DuplicateException();

        // Log Registration
        registrationRepository.save(newReg);
    }
}