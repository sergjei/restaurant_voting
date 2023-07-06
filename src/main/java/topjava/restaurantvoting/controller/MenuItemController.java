package topjava.restaurantvoting.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import topjava.restaurantvoting.model.MenuItem;
import topjava.restaurantvoting.repository.MenuItemRepository;
import topjava.restaurantvoting.repository.RestaurantRepository;
import topjava.restaurantvoting.to.MenuItemTo;
import topjava.restaurantvoting.utils.DateUtil;
import topjava.restaurantvoting.utils.MenuItemUtil;
import topjava.restaurantvoting.utils.ValidationUtil;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static topjava.restaurantvoting.utils.MenuItemUtil.updateFromToNoRest;

@RestController
@RequestMapping(value = MenuItemController.CURRENT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "basicAuth")
public class MenuItemController {
    public static final String CURRENT_URL = "/rest/admin/restaurants/{restaurant_id}/menu_items";
    public MenuItemRepository menuItemRepository;
    public RestaurantRepository restaurantRepository;

    public MenuItemController(MenuItemRepository menuItemRepository, RestaurantRepository restaurantRepository) {
        this.menuItemRepository = menuItemRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping
    public List<MenuItemTo> getAllByDate(@PathVariable("restaurant_id") Integer restaurantId,
                                         @RequestParam(value = "startDate") @Nullable LocalDate startDate,
                                         @RequestParam(value = "endDate") @Nullable LocalDate endDate) {
        return MenuItemUtil.getListTos(menuItemRepository.getByRestaurantAndDateInclusive(restaurantId,
                DateUtil.checkedStartDateOrMin(startDate),
                DateUtil.checkedEndDate(endDate)));
    }

    @GetMapping("/{id}")
    public MenuItemTo get(@PathVariable("id") Integer menuItemId, @PathVariable("restaurant_id") Integer restaurantId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find menuItem with  id = " + menuItemId));
        ValidationUtil.assureIdConsistentRest(menuItem.getRestaurant(), restaurantId);
        return MenuItemUtil.createToFrom(menuItem);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Integer menuItemId, @PathVariable("restaurant_id") Integer restaurantId) {
        MenuItem menuItem = menuItemRepository.findById(menuItemId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find menu item with  id = " + menuItemId));
        ValidationUtil.assureIdConsistentRest(menuItem.getRestaurant(), restaurantId);
        menuItemRepository.deleteById(menuItemId);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Integer menuItemId, @PathVariable("restaurant_id") Integer restaurantId,
                       @Valid @RequestBody MenuItemTo updatedTo) {
        MenuItem updated = updateFromToNoRest(updatedTo);
        ValidationUtil.assureIdConsistentDef(updated, menuItemId);
        updated.setRestaurant(restaurantRepository.findById(updatedTo.getRestaurant()).orElseThrow(
                () -> new EntityNotFoundException("Can`t find restaurant with  id = " + updatedTo.getRestaurant())
        ));
        if (!Objects.equals(updated.getRestaurant().getId(), restaurantId)) {
            throw new EntityNotFoundException("Menu item with id = " + menuItemId + " belongs to restaurant with id = " + updated.getRestaurant().getId());
        }
        ValidationUtil.assureIdConsistentRest(updated.getRestaurant(), restaurantId);
        menuItemRepository.save(updated);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MenuItemTo> create(@PathVariable("restaurant_id") Integer restaurantId,
                                             @Valid @RequestBody MenuItemTo menuItemTo) {
        MenuItem menuItem = updateFromToNoRest(menuItemTo);
        ValidationUtil.checkNew(menuItem);
        menuItem.setRestaurant(restaurantRepository.findById(menuItemTo.getRestaurant()).orElseThrow(
                () -> new EntityNotFoundException("Can`t find restaurant with  id = " + menuItemTo.getRestaurant())
        ));
        ValidationUtil.assureIdConsistentRest(menuItem.getRestaurant(), restaurantId);
        MenuItem created = menuItemRepository.save(menuItem);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(CURRENT_URL + "/{id}")
                .buildAndExpand(restaurantId, created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(MenuItemUtil.createToFrom(created));
    }
}
