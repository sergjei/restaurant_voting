package topjava.restaurantvoting.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import topjava.restaurantvoting.model.Meal;
import topjava.restaurantvoting.repository.MealRepository;
import topjava.restaurantvoting.repository.RestaurantRepository;
import topjava.restaurantvoting.utils.DateUtil;
import topjava.restaurantvoting.utils.ValidationUtil;
import topjava.restaurantvoting.utils.json.JsonUtil;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
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
                                   @RequestParam(value = "startDate") @Nullable LocalDate startDate,
                                   @RequestParam(value = "endDate") @Nullable LocalDate endDate) {
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
        if (meal.getRestaurant() == null) {
            meal.setRestaurant(restaurantRepository.findById(restId).orElseThrow());
        } else {
            ValidationUtil.assureIdConsistent(meal.getRestaurant(), restId);
        }
        Meal created = mealRepository.save(meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(CURRENT_URL + "/{id}")
                .buildAndExpand(restId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PostMapping(value = "/updateMenu", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<Meal>> updateMenu(@PathVariable("rest_id") Integer restId,
                                                 @RequestBody String json) {
        List<String> jsonMeals = JsonUtil.jsonToStringList(json);
        List<Meal> meals = new ArrayList<>();
        for (String jsonMeal : jsonMeals) {
            Meal meal = JsonUtil.readValue(jsonMeal, Meal.class);
            meals.add(create(restId, meal).getBody());
        }
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(CURRENT_URL)
                .buildAndExpand(restId).toUri();
        return ResponseEntity.created(uriOfNewResource).body(meals);
    }
}
