package edu.lawrence.friendfinder.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.lawrence.friendfinder.entities.Event;
import edu.lawrence.friendfinder.entities.GenreTag;
import edu.lawrence.friendfinder.entities.PlatformTag;
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
	
	public List<Event> findEvents(UUID userid) {
		Optional<User> maybeUser = userRepository.findById(userid);
		if(!maybeUser.isPresent())
			return new ArrayList<Event>();
		return maybeUser.get().getEvents();
	}
	
	private boolean tagInPlatforms(List<PlatformTag> platforms, String tag) {
		for (PlatformTag t : platforms) {
			if (t.getName().equals(tag)) 
				return true;
		}
		
		return false;
	}
	
	private boolean tagInGenres(List<GenreTag> genres, String tag) {
		for (GenreTag t : genres) {
			if (t.getName().equals(tag))
				return true;
		}
		
		return false;
	}
	
	// Returns a list of all profileDTOs that contain the specified tags
	// Will return a DTO if any of its tags match any of the specified tags
	// If one list is empty then it will only consider the other
	// If both lists are empty then it will return all profiles
	public List<ProfileDTO> getProfilesByTags(List<String> platformTags, List<String> genreTags) {
		// I couldn't figure out a way to manipulate the hibernate query to perform this
		// filtering logic for me
		// I'm sure there's a way I just couldn't find any examples of how to perform
		// list comparison in HQL
		// The *other* alternative was performing a bunch of query-calls with filters
		// applied
		// I felt that telling the server to interact with the SQL server might cause
		// noticable performance issues
		List<Profile> lgResults = profileRepository.findAll();
		List<Profile> results = new ArrayList<Profile>();

		for (Profile p : lgResults) {
			boolean add = platformTags.size() <= 0 && genreTags.size() <= 0;

			if (!add || platformTags.size() > 0 ) {
				for (String t : platformTags) {
					if (tagInPlatforms(p.getPlatforms(), t)) {
						add = true;
						break;
					}
				}
			}

			if (!add || genreTags.size() > 0) {
				for (String t : genreTags) {
					if (tagInGenres(p.getGenres(), t)) {
						add = true;
						break;
					}
				}
			}

			// You can use Collections.contains() for this, but I'm worried about how java
			// will evaluate full obj comparisons
			// Comparing UUIDs of corresponding users seems a sensible alternative
			if (add) {
				// True == already added, False == not already added
				boolean da = false;
				for (Profile dp : results) {
					if (p.getUser().getId().equals(dp.getUser().getId())) {
						da = true;
						break;
					}
				}

				if (!da)
					results.add(p);

			}
		}

		List<ProfileDTO> ret = new ArrayList<ProfileDTO>();
		results.forEach((p) -> {
			ProfileDTO np = new ProfileDTO(p);
			ret.add(np);
		});

		return ret;
	}
	
	// Refer to getProfilesByTags() for all the same comments
	// This version is 'exclusive', so will return a list where only all submitted tags match
	// I.e., a profile must have *all* the specified platform and genre tags to be returned
	// If one list is empty, it is disregarded in the matching process
	// If both lists are empty, all profiles are returned
	public List<ProfileDTO> getProfilesByTagsEx(List<String> platformTags, List<String> genreTags) {
		List<Profile> lgResults = profileRepository.findAll();
		List<Profile> results = new ArrayList<Profile>();
		
		for (Profile p : lgResults) {
			boolean add = true;

			if (platformTags.size() > 0 && add) {
				for (String t : platformTags) {
					if (!tagInPlatforms(p.getPlatforms(), t)) {
						add = false;
						break;
					}
				}
			}

			if (genreTags.size() > 0 && add) {
				for (String t : genreTags) {
					if (!tagInGenres(p.getGenres(), t)) {
						add = false;
						break;
					}
				}
			}

			if (add) {
				boolean da = false;
				for (Profile dp : results) {
					if (p.getUser().getId().equals(dp.getUser().getId())) {
						da = true;
						break;
					}
				}

				if (!da)
					results.add(p);

			}
		}

		List<ProfileDTO> ret = new ArrayList<ProfileDTO>();
		results.forEach((p) -> {
			ProfileDTO np = new ProfileDTO(p);
			ret.add(np);
		});

		return ret;
	}
}