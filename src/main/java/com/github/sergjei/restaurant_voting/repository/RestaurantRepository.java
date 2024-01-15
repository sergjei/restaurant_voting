package com.github.sergjei.restaurant_voting.repository;

import com.github.sergjei.restaurant_voting.model.Restaurant;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true)
public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {
    @Query("SELECT r FROM Restaurant r INNER JOIN FETCH r.menu m WHERE m.menuDate=:date")
    List<Restaurant> getRestaurantWithMenuByDate(@Param("date") LocalDate date);

    Optional<Restaurant> findById(Integer id);

    @Modifying
    @Transactional
    //potentially, restaurant can be created with menu, so every change of restaurant evicts "menu" cache
    @CacheEvict(value = "menu", allEntries = true)
    Restaurant save(Restaurant restaurant);

    @Modifying
    @Transactional
    @CacheEvict(value = "menu", allEntries = true)
    void deleteById(Integer id);
}
