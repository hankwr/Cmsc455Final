package edu.lawrence.friendfinder.repositories;

// In-project includes [Entities]
import edu.lawrence.friendfinder.entities.Registration;

// Java-level includes [Utility]
import java.util.List;
import java.util.UUID;

// Spring-level includes [Interfaces]
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationRepository extends JpaRepository<Registration, Integer>{

	List<Registration> findByEvent(Integer eventid);
	
	List<Registration> findByUser(UUID userid);
	
}