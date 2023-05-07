package topjava.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import topjava.restaurantvoting.model.Restaurant;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Query("SELECT r FROM Restaurant r INNER JOIN FETCH r.menu m WHERE m.menuDate=CURRENT_DATE()")
    List<Restaurant> getTodayMenu();
}
