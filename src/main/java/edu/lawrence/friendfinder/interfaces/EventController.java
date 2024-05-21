package edu.lawrence.friendfinder.interfaces;

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

import edu.lawrence.friendfinder.exceptions.DuplicateException;
import edu.lawrence.friendfinder.interfaces.dtos.EventDTO;
import edu.lawrence.friendfinder.security.AppUserDetails;
import edu.lawrence.friendfinder.services.EventService;

@RestController
@RequestMapping("/events")
@CrossOrigin(origins = "*")
public class EventController {
	private EventService es;

    public EventController() {}

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
        } 
        return ResponseEntity.status(HttpStatus.CREATED).body(event);
    }
}
