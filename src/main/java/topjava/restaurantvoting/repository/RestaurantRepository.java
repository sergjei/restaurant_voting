package topjava.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import topjava.restaurantvoting.model.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

}
