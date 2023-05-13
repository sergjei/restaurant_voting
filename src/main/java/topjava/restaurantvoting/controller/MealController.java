package topjava.restaurantvoting.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import topjava.restaurantvoting.model.Meal;
import topjava.restaurantvoting.repository.MealRepository;
import topjava.restaurantvoting.repository.RestaurantRepository;
import topjava.restaurantvoting.to.MealTo;
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
    public static final String CURRENT_URL = "/rest/admin/restaurants/{rest_id}/meals";
    public MealRepository mealRepository;
    public RestaurantRepository restaurantRepository;

    public MealController(MealRepository mealRepository, RestaurantRepository restaurantRepository) {
        this.mealRepository = mealRepository;
        this.restaurantRepository = restaurantRepository;

    }

    @GetMapping
    public List<MealTo> getAllByDate(@PathVariable("rest_id") Integer restId,
                                     @RequestParam(value = "startDate") @Nullable LocalDate startDate,
                                     @RequestParam(value = "endDate") @Nullable LocalDate endDate) {
        return MealTo.getListTos(mealRepository.getByRestaurantAndDateInclusive(restId,
                DateUtil.checkedStartDateOrMin(startDate),
                DateUtil.checkedEndDate(endDate)));
    }

    @GetMapping("/{id}")
    public MealTo get(@PathVariable("id") Integer mealId, @PathVariable("rest_id") Integer restId) {
        Meal meal = mealRepository.findById(mealId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find meal with  id = " + mealId));
        ValidationUtil.assureIdConsistentRest(meal.getRestaurant(), restId);
        return MealTo.createFrom(meal);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Integer mealId, @PathVariable("rest_id") Integer restId) {
        Meal meal = mealRepository.findById(mealId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find meal with  id = " + mealId));
        ValidationUtil.assureIdConsistentRest(meal.getRestaurant(), restId);
        mealRepository.deleteById(mealId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Integer mealId, @PathVariable("rest_id") Integer restId,
                       @Valid @RequestBody MealTo updatedTo) {
        Meal updated = new Meal();
        updated.updateFromToNoRest(updatedTo);
        ValidationUtil.assureIdConsistentDef(updated, mealId);
        updated.setRestaurant(restaurantRepository.findById(updatedTo.getRestaurant()).orElseThrow(
                () -> new EntityNotFoundException("Can`t find restaurant with  id = " + updatedTo.getRestaurant())
        ));
        ValidationUtil.assureIdConsistentRest(updated.getRestaurant(), restId);
        mealRepository.save(updated);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MealTo> create(@PathVariable("rest_id") Integer restId,
                                         @Valid @RequestBody MealTo mealTo) {
        Meal meal = new Meal();
        meal.updateFromToNoRest(mealTo);
        ValidationUtil.checkNew(meal);
        meal.setRestaurant(restaurantRepository.findById(mealTo.getRestaurant()).orElseThrow(
                () -> new EntityNotFoundException("Can`t find restaurant with  id = " + mealTo.getRestaurant())
        ));
        ValidationUtil.assureIdConsistentRest(meal.getRestaurant(), restId);
        Meal created = mealRepository.save(meal);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(CURRENT_URL + "/{id}")
                .buildAndExpand(restId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(MealTo.createFrom(created));
    }

    @PostMapping(value = "/updateMenu", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<List<MealTo>> updateMenu(@PathVariable("rest_id") Integer restId,
                                                   @Parameter(description = "List of meals with restaurant replaced by its id",
                                                           required = true,
                                                           array = @ArraySchema(schema = @Schema(implementation = MealTo.class)))
                                                   @RequestBody String json) {
        List<String> jsonMeals = JsonUtil.jsonToStringList(json);
        List<MealTo> meals = new ArrayList<>();
        for (String jsonMeal : jsonMeals) {
            MealTo mealTo = MealTo.createFrom(JsonUtil.readValue(jsonMeal, Meal.class));
            meals.add(create(restId, mealTo).getBody());
        }
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(CURRENT_URL)
                .buildAndExpand(restId).toUri();
        return ResponseEntity.created(uriOfNewResource).body(meals);
    }
}
