package topjava.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import topjava.restaurantvoting.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    User getByEmail(String email);
}
