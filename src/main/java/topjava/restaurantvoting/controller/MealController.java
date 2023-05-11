package topjava.restaurantvoting.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import topjava.restaurantvoting.utils.DateUtil;
import topjava.restaurantvoting.utils.ValidationUtil;
import topjava.restaurantvoting.model.Meal;
import topjava.restaurantvoting.repository.MealRepository;
import topjava.restaurantvoting.repository.RestaurantRepository;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@Validated
@RequestMapping(value = MealController.CURRENT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealController {
    public static final String CURRENT_URL = "/rest/admin/restaurant/{rest_id}/meal";
    public MealRepository mealRepository;
    public RestaurantRepository restaurantRepository;

    public MealController(MealRepository mealRepository, RestaurantRepository restaurantRepository) {
        this.mealRepository = mealRepository;
        this.restaurantRepository = restaurantRepository;

    }

    @GetMapping
    public List<Meal> getAllByDate(@PathVariable("rest_id") Integer restId,
                                   @RequestParam @Nullable LocalDate startDate,
                                   @RequestParam @Nullable LocalDate endDate) {
        return mealRepository.getByRestaurantAndDateInclusive(restId,
                DateUtil.checkedStartDateOrMin(startDate),
                DateUtil.checkedEndDate(endDate));
    }

    @GetMapping("/{id}")
    public Meal get(@PathVariable("id") Integer mealId, @PathVariable("rest_id") Integer restId) {
        Meal meal = mealRepository.findById(mealId).orElseThrow();
        ValidationUtil.assureIdConsistent(meal.getRestaurant(), restId);
        return meal;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Integer mealId, @PathVariable("rest_id") Integer restId) {
        Meal meal = mealRepository.findById(mealId).orElseThrow();
        ValidationUtil.assureIdConsistent(meal.getRestaurant(), restId);
        mealRepository.deleteById(mealId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Integer mealId, @PathVariable("rest_id") Integer restId,
                       @RequestBody Meal updated) {
        ValidationUtil.assureIdConsistent(updated, mealId);
        ValidationUtil.assureIdConsistent(updated.getRestaurant(), restId);
        mealRepository.save(updated);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Meal> create(@PathVariable("rest_id") Integer restId,
                                       @Valid @RequestBody Meal meal) {
        ValidationUtil.checkNew(meal);
        ValidationUtil.assureIdConsistent(meal.getRestaurant(), restId);
        Meal created = mealRepository.save(meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(CURRENT_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<Meal>> updateMenu(@PathVariable("rest_id") Integer restId,
                                                 @Valid @RequestBody List<Meal> meals) { //https://stackoverflow.com/questions/28150405/validation-of-a-list-of-objects-in-spring
        for (Meal m : meals) {
            ValidationUtil.checkNew(m);
            ValidationUtil.assureIdConsistent(m.getRestaurant(), restId);
        }
        List<Meal> created = mealRepository.saveAll(meals);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(CURRENT_URL)
                .build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }
}
