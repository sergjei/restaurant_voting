package topjava.restaurantvoting.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import topjava.restaurantvoting.model.Meal;
import topjava.restaurantvoting.model.Role;
import topjava.restaurantvoting.model.User;
import topjava.restaurantvoting.repository.MealRepository;
import topjava.restaurantvoting.repository.RestaurantRepository;
import topjava.restaurantvoting.repository.UserRepository;
import topjava.restaurantvoting.repository.VoteRepository;

import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = MealController.CURRENT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealController {
    public static final String CURRENT_URL = "/rest/admin/restaurant/{restId}/meal";
    public MealRepository mealRepository;
    public RestaurantRepository restaurantRepository;

    public MealController(MealRepository mealRepository, RestaurantRepository restaurantRepository) {
        this.mealRepository = mealRepository;
        this.restaurantRepository = restaurantRepository;

    }
    @GetMapping
    public List<Meal> getAll(@PathVariable("restId") Integer restId) {
        return mealRepository.findAll();
    }
    @GetMapping("/{id}")
    public Meal get(@PathVariable("restId") Integer restId,@PathVariable("id") Integer mealId) {
        return mealRepository.findById(mealId).orElseThrow();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("restId") Integer restId,@PathVariable("id") Integer mealId, @RequestBody Meal updated) {
        updated.setId(mealId);
        mealRepository.save(updated);
    }

//    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
//    @ResponseStatus(HttpStatus.CREATED)
//    public ResponseEntity<Meal> create(@PathVariable("restId") Integer restId, @RequestBody Meal meal) {
//        Meal created = mealRepository.save(meal);
//        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
//                .path(CURRENT_URL+"/{id}")
//                .buildAndExpand(created.getId()).toUri();
//        return ResponseEntity.created(uriOfNewResource).body(created);
//    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("restId") Integer restId, @PathVariable("id") Integer mealId) {
        mealRepository.deleteById(mealId);
    }
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<Meal>> updateMenu(@PathVariable("restId") Integer restId, @RequestBody List<Meal> meals) {
        List<Meal> created = mealRepository.saveAll(meals);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(CURRENT_URL)
                .build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }


}
