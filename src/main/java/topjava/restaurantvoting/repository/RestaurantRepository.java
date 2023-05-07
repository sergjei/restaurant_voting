package topjava.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import topjava.restaurantvoting.model.Restaurant;

import java.util.List;
@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Query("SELECT r FROM Restaurant r INNER JOIN FETCH r.menu m WHERE m.menuDate=CURRENT_DATE()")
    List<Restaurant> getTodayMenu();

}
