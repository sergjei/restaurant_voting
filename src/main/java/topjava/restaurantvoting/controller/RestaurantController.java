package topjava.restaurantvoting.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import topjava.restaurantvoting.model.Restaurant;
import topjava.restaurantvoting.repository.MealRepository;
import topjava.restaurantvoting.repository.RestaurantRepository;
import topjava.restaurantvoting.to.RestaurantTo;
import topjava.restaurantvoting.utils.ValidationUtil;

import java.net.URI;
import java.util.List;

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

    @GetMapping
    public List<RestaurantTo> getAll() {
        return RestaurantTo.getListTos(restaurantRepository.findAll());
    }

    @GetMapping("/{id}")
    public RestaurantTo get(@PathVariable Integer id) {
        return RestaurantTo.createFrom(restaurantRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find restaurant with  id = " + id)
        ));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        restaurantRepository.deleteById(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @Valid @RequestBody RestaurantTo updated) {
        ValidationUtil.assureIdConsistentDef(updated, id);
        restaurantRepository.save(Restaurant.createFromTo(updated));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RestaurantTo> create(@Valid @RequestBody RestaurantTo restaurant) {
        ValidationUtil.checkNew(restaurant);
        RestaurantTo created = RestaurantTo.createFrom(restaurantRepository.save(Restaurant.createFromTo(restaurant)));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(CURRENT_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
