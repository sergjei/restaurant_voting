package topjava.restaurantvoting.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import topjava.restaurantvoting.model.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Cacheable(cacheNames = {"menu"})
    @Query("SELECT r FROM Restaurant r INNER JOIN FETCH r.menu m WHERE m.menuDate=:date")
    List<Restaurant> getRestaurantWithMenuByDate(@Param("date") LocalDate date);

    @Cacheable(cacheNames = "restaurants")
    Optional<Restaurant> findById(Integer id);

    @Override
    @Modifying
    @Transactional
    @CachePut(value = "restaurants", key = "#restaurant.id")
    Restaurant save(Restaurant restaurant);

    @Override
    @Modifying
    @Transactional
    @CacheEvict(value = "restaurants", allEntries = true)
    void deleteById(Integer id);
}
