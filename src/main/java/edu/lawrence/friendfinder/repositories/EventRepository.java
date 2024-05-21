package edu.lawrence.friendfinder.repositories;

// In-project includes [Entities]
import edu.lawrence.friendfinder.entities.Event;
import edu.lawrence.friendfinder.entities.User;

// Java-level includes [Utility]
import java.time.Instant;
import java.util.List;

// Spring-level includes [Interfaces]
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EventRepository extends JpaRepository<Event, Integer>{
	List<Event> findByName(String name);
	List<Event> findByHost(User host);

	@Query("select e from Event a where startTime>=:i")
	List<Event> findFuture(Instant i);
}