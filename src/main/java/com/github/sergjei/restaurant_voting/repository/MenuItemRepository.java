package com.github.sergjei.restaurant_voting.repository;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.github.sergjei.restaurant_voting.model.MenuItem;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
    @Query("SELECT m FROM MenuItem m WHERE m.restaurant.id =:restaurantId AND m.menuDate >= :startDate AND m.menuDate<=:endDate ")
    List<MenuItem> getByRestaurantAndDateInclusive(@Param("restaurantId") Integer restaurantId,
                                                   @Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);

    @Modifying
    @Transactional
    @CacheEvict(value = "menu", allEntries = true)
    MenuItem save(MenuItem menuItem);

    @Modifying
    @Transactional
    @CacheEvict(value = "menu", allEntries = true)
    void deleteById(Integer id);
}
