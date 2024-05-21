package edu.lawrence.friendfinder.repositories;

// In-project includes [Entities]
import edu.lawrence.friendfinder.entities.Event;

// Java-level includes [Utility]
import java.util.List;
import java.util.UUID;

// Spring-level includes [Interfaces]
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Integer>{
	List<Event> findByName(String name);

	// Broken method b/c of diferences btw user & profile
	//List<Event> findByProfile(UUID userid);
}