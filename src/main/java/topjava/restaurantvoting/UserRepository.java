package topjava.restaurantvoting;

import org.springframework.data.jpa.repository.JpaRepository;
import topjava.restaurantvoting.model.User;

public interface UserRepository extends JpaRepository<User,Long> {
}
