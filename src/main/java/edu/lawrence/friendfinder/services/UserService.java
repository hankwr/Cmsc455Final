package edu.lawrence.friendfinder.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.lawrence.friendfinder.entities.Profile;
import edu.lawrence.friendfinder.entities.User;
import edu.lawrence.friendfinder.exceptions.DuplicateException;
import edu.lawrence.friendfinder.exceptions.UnauthorizedException;
import edu.lawrence.friendfinder.interfaces.dtos.ProfileDTO;
import edu.lawrence.friendfinder.interfaces.dtos.UserDTO;
import edu.lawrence.friendfinder.repositories.ProfileRepository;
import edu.lawrence.friendfinder.repositories.UserRepository;

@Service
public class UserService {
	@Autowired 
    PasswordService passwordService;
	
	@Autowired
	UserRepository userRepository;

    @Autowired
    ProfileRepository profileRepository;
	
    // Register new user
	public String save(UserDTO user) throws DuplicateException {
		List<User> existing = userRepository.findByUsername(user.getUsername());
		if(existing.size() > 0)
			throw new DuplicateException();
		
		User newUser = new User();
		newUser.setUsername(user.getUsername());
		String hash = passwordService.hashPassword(user.getPassword());
	    newUser.setPassword(hash);
		userRepository.save(newUser);
		return newUser.getId().toString();
	}

	// Log-in
	public User findByNameAndPassword(String name,String password) {
		if(name.isBlank() || password.isBlank())
			return null;
		
		List<User> existing = userRepository.findByUsername(name);
		if(existing.size() != 1)
			return null;
		User u = existing.get(0);
		if(passwordService.verifyHash(password, u.getPassword())) {
        	u.setPassword("Undisclosed");
        } else {
        	u = null;
        }
        return u;
	}
	
	public void saveProfile(UUID userid,ProfileDTO profile) throws UnauthorizedException, DuplicateException {
		Optional<User> maybeUser = userRepository.findById(userid);
		/*if(!maybeUser.isPresent())
			throw new UnauthorizedException();
		*/
		User user = maybeUser.get();
		if(user.getProfile() != null)
			throw new DuplicateException();
		
		Profile newProfile = new Profile(profile);
		newProfile.setUser(user);
		profileRepository.save(newProfile);
	}
	
	public Profile findProfile(UUID userid) {
		Optional<User> maybeUser = userRepository.findById(userid);
		if(!maybeUser.isPresent())
			return null;
		
		return maybeUser.get().getProfile();
	}
}