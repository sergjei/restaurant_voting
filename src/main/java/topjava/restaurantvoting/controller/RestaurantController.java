package topjava.restaurantvoting.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import topjava.restaurantvoting.repository.RestaurantRepository;
import topjava.restaurantvoting.to.RestaurantTo;
import topjava.restaurantvoting.utils.RestaurantsUtil;
import topjava.restaurantvoting.utils.ValidationUtil;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = RestaurantController.CURRENT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "basicAuth")
public class RestaurantController {

    public static final String CURRENT_URL = "/rest/admin/restaurants";
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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        restaurantRepository.deleteById(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @Valid @RequestBody RestaurantTo updated) {
        ValidationUtil.assureIdConsistentDef(updated, id);
        restaurantRepository.save(RestaurantsUtil.createFromTo(updated));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RestaurantTo> create(@Valid @RequestBody RestaurantTo restaurant) {
        ValidationUtil.checkNew(restaurant);
        RestaurantTo created = RestaurantsUtil.createToFrom(restaurantRepository.save(RestaurantsUtil.createFromTo(restaurant)));
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(CURRENT_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
