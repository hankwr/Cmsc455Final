package edu.lawrence.friendfinder.repositories;

// In-project includes [Entities]
import edu.lawrence.friendfinder.entities.User;

// Java-level includes [Utility]
import java.util.List;
import java.util.UUID;

// Spring-level includes [Interfaces]
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID>{
	
	List<User> findByUsername(String username);
	
}