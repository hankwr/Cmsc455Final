package edu.lawrence.friendfinder.interfaces;

import edu.lawrence.friendfinder.services.JwtService;
import edu.lawrence.friendfinder.services.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.config.authentication.UserServiceBeanDefinitionParser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edu.lawrence.friendfinder.entities.Event;
import edu.lawrence.friendfinder.entities.Profile;
import edu.lawrence.friendfinder.entities.User;
import edu.lawrence.friendfinder.exceptions.DuplicateException;
import edu.lawrence.friendfinder.exceptions.UnauthorizedException;
import edu.lawrence.friendfinder.interfaces.dtos.EventDTO;
import edu.lawrence.friendfinder.interfaces.dtos.ProfileDTO;
import edu.lawrence.friendfinder.interfaces.dtos.UserDTO;
import edu.lawrence.friendfinder.security.AppUserDetails;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    private UserService us;

    private JwtService jwtService;
    
    public UserController(UserService us, JwtService jwtService) {
        this.us = us;
        this.jwtService = jwtService;
    }

    // Register new user
    @PostMapping
    public ResponseEntity<UserDTO> save(@RequestBody UserDTO user) {
        if (user.getUsername().isBlank() || user.getPassword().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(user);
        }
        String key;
        try {
        key = us.save(user);
        } catch(DuplicateException ex){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(user);
        } 
        String token = jwtService.makeJwt(key);
        user.setToken(token);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    
    @PostMapping("/login")
    public ResponseEntity<UserDTO> checkLogin(@RequestBody UserDTO user) {
        User result = us.findByNameAndPassword(user.getUsername(), user.getPassword());
        if (result == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(user);
        }
        String token = jwtService.makeJwt(result.getId().toString());
        user.setToken(token);
        return ResponseEntity.ok().body(user);
    }
    
    @PostMapping("/profile")
    public ResponseEntity<ProfileDTO> saveProfile(Authentication authentication,@RequestBody ProfileDTO profile) {
    	AppUserDetails details = (AppUserDetails) authentication.getPrincipal();
    	UUID id = UUID.fromString(details.getUsername());
    	try {
    		us.saveProfile(id,profile);
    	} catch(UnauthorizedException ex) {
    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(profile);
    	} catch(DuplicateException ex) {
    		return ResponseEntity.status(HttpStatus.CONFLICT).body(profile);
    	}
    	return ResponseEntity.status(HttpStatus.CREATED).body(profile);
    }
    
    @GetMapping("/profile")
    public ResponseEntity<ProfileDTO> getProfile(Authentication authentication) {
    	AppUserDetails details = (AppUserDetails) authentication.getPrincipal();
    	UUID id = UUID.fromString(details.getUsername());
    	Profile result = us.findProfile(id);
    	if(result == null) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    	}
    	ProfileDTO response = new ProfileDTO(result);
    	return ResponseEntity.ok().body(response);
    }
    
    @GetMapping("/events")
	public ResponseEntity<List<EventDTO>> getEvents(Authentication authentication) {
    	AppUserDetails details = (AppUserDetails) authentication.getPrincipal();
    	UUID id = UUID.fromString(details.getUsername());
    	List<Event> events = us.findEvents(id);
    	List<EventDTO> results = new ArrayList<EventDTO>();
        for(Event e : events) {
			results.add(new EventDTO(e));
		}
		return ResponseEntity.ok().body(results);
	}
    
    // Could also convert this to request body mapping since this might
    // 		clutter the searchbar
    // http://.../$platform=tag1&platform=tag2&genre=tag3&genre=tag4&excl=false
    // http://.../$excl=false
//    @GetMapping(value = "/profile", params = {"platform", "genre", "excl"})
//    public ResponseEntity<List<ProfileDTO>> getProfileWithTags(Authentication authentication, 
//    		@RequestParam(value = "platform", required = false) List<String> platformTags,
//    		@RequestParam(value = "genre", required = false) List<String> genreTags,
//    		@RequestParam(value = "excl") boolean exclude)
//    {
//    	AppUserDetails details = (AppUserDetails) authentication.getPrincipal();
//    	
//    	List<ProfileDTO> results = new ArrayList<ProfileDTO>();
//    	
//    	// Prevented non-users from accessing other user profiles
//    	UUID id = UUID.fromString(details.getUsername());
//    	Profile checkUser = us.findProfile(id);
//    	if (checkUser == null)
//    		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(results);
//    	
//    	if (exclude)
//    		results = us.getProfilesByTagsEx(platformTags, genreTags);
//    	else
//    		results = us.getProfilesByTags(platformTags, genreTags);
//    	
//    	return ResponseEntity.ok().body(results);
//    }
}