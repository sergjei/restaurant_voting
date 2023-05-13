package topjava.restaurantvoting.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import topjava.restaurantvoting.model.Meal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface MealRepository extends JpaRepository<Meal, Integer> {
    @Query("SELECT m FROM Meal m WHERE m.restaurant.id =:restId AND m.menuDate >= :startDate AND m.menuDate<=:endDate ")
    List<Meal> getByRestaurantAndDateInclusive(@Param("restId") Integer restaurantId,
                                               @Param("startDate") LocalDate startDate,
                                               @Param("endDate") LocalDate endDate);

    @Override
    @Query("SELECT m FROM Meal m JOIN FETCH m.restaurant WHERE m.id =:id ")
    Optional<Meal> findById(@Param("id") Integer id);

    @Override
    @Query("SELECT m FROM Meal m JOIN FETCH m.restaurant")
    List<Meal> findAll();

    @Override
    @Modifying
    @Transactional
    @CachePut(value = "menu", key = "#meal.id")
    Meal save(Meal meal);

    @Override
    @Modifying
    @Transactional
    @CacheEvict(value = "menu", allEntries = true)
    void deleteById(Integer id);
}
