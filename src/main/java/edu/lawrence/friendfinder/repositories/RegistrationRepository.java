package edu.lawrence.friendfinder.repositories;

// In-project includes [Entities]
import edu.lawrence.friendfinder.entities.Registration;

// Java-level includes [Utility]
import java.util.Optional;
import java.util.UUID;

// Spring-level includes [Interfaces]
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RegistrationRepository extends JpaRepository<Registration, Integer>{
	@Query("select r from Registration r where event.id=:eventid and user.id=:userid")
	Optional<Registration> checkForDuplicates(Integer eventid, UUID userid);
	/*
	List<Registration> findByEvent(Integer eventid);
	
	List<Registration> findByUser(UUID userid);
	*/
}