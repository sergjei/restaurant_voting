package topjava.restaurantvoting.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import topjava.restaurantvoting.model.Meal;
import topjava.restaurantvoting.model.Restaurant;
import topjava.restaurantvoting.repository.MealRepository;
import topjava.restaurantvoting.repository.RestaurantRepository;

import java.net.URI;

@RestController
@RequestMapping(value = RestaurantController.CURRENT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantController {

    public static final String CURRENT_URL = "/rest/admin/restaurant";
    public MealRepository mealRepository;
    public RestaurantRepository restaurantRepository;

    public RestaurantController(MealRepository mealRepository, RestaurantRepository restaurantRepository) {
        this.mealRepository = mealRepository;
        this.restaurantRepository = restaurantRepository;
    }
    @GetMapping("/{id}")
    public Restaurant get(@PathVariable Integer restId) {
        return restaurantRepository.findById(restId).orElseThrow();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer restId, @RequestBody Restaurant updated) {
        updated.setId(restId);
        restaurantRepository.save(updated);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Restaurant> create(@RequestBody Restaurant restaurant) {
        Restaurant created = restaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(CURRENT_URL+"/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer restId) {
        restaurantRepository.deleteById(restId);
    }
    
}
