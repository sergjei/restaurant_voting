package topjava.restaurantvoting.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import topjava.restaurantvoting.model.MenuItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id =:restaurantId AND m.menuDate >= :startDate AND m.menuDate<=:endDate ")
    List<MenuItem> getByRestaurantAndDateInclusive(@Param("restaurantId") Integer restaurantId,
                                                   @Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);

    @Override
    @Query("SELECT m FROM MenuItem m JOIN FETCH m.restaurant WHERE m.id =:id ")
    Optional<MenuItem> findById(@Param("id") Integer id);

    @Override
    @Query("SELECT m FROM MenuItem m JOIN FETCH m.restaurant")
    List<MenuItem> findAll();

    @Override
    @Modifying
    @Transactional
    @CachePut(value = "menu", key = "#menuItem.id")
    MenuItem save(MenuItem menuItem);

    @Override
    @Modifying
    @Transactional
    @CacheEvict(value = "menu", allEntries = true)
    void deleteById(Integer id);
}
