package com.github.sergjei.restaurant_voting.controller;

import com.github.sergjei.restaurant_voting.repository.RestaurantRepository;
import com.github.sergjei.restaurant_voting.to.RestaurantTo;
import com.github.sergjei.restaurant_voting.utils.DateUtil;
import com.github.sergjei.restaurant_voting.utils.RestaurantsUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.CURRENT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "basicAuth")
@Transactional
public class RestaurantController {

    public static final String CURRENT_URL = "/rest/restaurants";
    public RestaurantRepository restaurantRepository;

    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping
    public List<RestaurantTo> getAll() {
        return RestaurantsUtil.getListTos(restaurantRepository.findAll());
    }

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable Integer id) {
        return RestaurantsUtil.createToFrom(restaurantRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find restaurant with  id = " + id)
        ));
    }

    @Cacheable(cacheNames = "menu")
    @GetMapping("/todaymenu")
    public List<RestaurantTo> getMenu() {
        return RestaurantsUtil.getListTosWithMenu(restaurantRepository.getRestaurantWithMenuByDate(DateUtil.getToday()));
    }
}
