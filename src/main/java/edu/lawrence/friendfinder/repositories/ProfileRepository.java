package edu.lawrence.friendfinder.repositories;

// In-project includes [Entities]
import edu.lawrence.friendfinder.entities.Profile;

// Java-level includes [Utility]
import java.util.Optional;
import java.util.UUID;

// Spring-level includes [Interfaces]
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileRepository extends JpaRepository<Profile, Integer>{
	
	Optional<Profile> findByUserId(UUID userid);
	
}