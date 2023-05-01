package topjava.restaurantvoting.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import topjava.restaurantvoting.model.Meal;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface MealRepository extends JpaRepository<Meal, Integer> {

    @Query("SELECT m FROM Meal m WHERE m.restaurant.id =:restId ")
    List<Meal> getByRestaurant(@Param("restId") Integer restId);

    @Query("SELECT m FROM Meal m WHERE m.restaurant.id =:restId AND m.menuDate >= :startDate AND m.menuDate<=:endDate ")
    List<Meal> getByRestaurantAndDateInclusive(@Param("restId") Integer restaurantId,
                                               @Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);

}
