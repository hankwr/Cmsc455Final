package edu.lawrence.friendfinder.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.lawrence.friendfinder.entities.Event;
import edu.lawrence.friendfinder.exceptions.DuplicateException;
import edu.lawrence.friendfinder.exceptions.InvalidException;
import edu.lawrence.friendfinder.interfaces.dtos.EventDTO;
import edu.lawrence.friendfinder.security.AppUserDetails;
import edu.lawrence.friendfinder.services.EventService;

@RestController
@RequestMapping("/events")
@CrossOrigin(origins = "*")
public class EventController {
	private EventService es;

    public EventController(EventService es) {
		this.es = es;
	}

    // Register new user
    @PostMapping
    public ResponseEntity<EventDTO> save(Authentication authentication,@RequestBody EventDTO event) {
		AppUserDetails details = (AppUserDetails) authentication.getPrincipal();
    	UUID id = UUID.fromString(details.getUsername());
		event.setUserid(id);
        if (event.getUserid().toString().isBlank() || event.getName().isBlank() || event.getLocation().isBlank() || event.getStartTime().toString().isBlank() || event.getEndTime().toString().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(event);
        }
        try {
        	es.save(event);
        } catch(DuplicateException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(event);
        } catch(InvalidException ex) {
        	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(event);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }

    @GetMapping("/{id}")
	public ResponseEntity<EventDTO> findById(@PathVariable Integer id) {
		Event event = es.findById(id);
		EventDTO result = new EventDTO(event);
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping
	public ResponseEntity<List<EventDTO>> findFutureEvents() {
		List<Event> events = es.findFuture();
		List<EventDTO> results = new ArrayList<EventDTO>();
		for(Event e : events) {
			results.add(new EventDTO(e));
		}
		return ResponseEntity.ok().body(results);
	}
}
