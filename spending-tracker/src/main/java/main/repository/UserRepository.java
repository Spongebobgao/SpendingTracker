package main.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import main.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

	User findByEmail(String email);
	User findByPassword(String password);
}
