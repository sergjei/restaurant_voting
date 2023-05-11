package topjava.restaurantvoting.controller;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import topjava.restaurantvoting.utils.DateUtil;
import topjava.restaurantvoting.utils.ValidationUtil;
import topjava.restaurantvoting.model.BaseEntity;
import topjava.restaurantvoting.model.Restaurant;
import topjava.restaurantvoting.model.User;
import topjava.restaurantvoting.model.Vote;
import topjava.restaurantvoting.repository.RestaurantRepository;
import topjava.restaurantvoting.repository.UserRepository;
import topjava.restaurantvoting.repository.VoteRepository;
import topjava.restaurantvoting.to.RestaurantTo;

import java.net.URI;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping(value = AdminController.CURRENT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {
    public static final String CURRENT_URL = "/rest/admin/users";
    public UserRepository userRepository;
    public RestaurantRepository restaurantRepository;
    public VoteRepository voteRepository;

    public AdminController(UserRepository userRepository, RestaurantRepository restaurantRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.voteRepository = voteRepository;
    }

    @GetMapping
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Integer id) {
        return userRepository.findById(id).orElseThrow();
    }

    @GetMapping("/user_by_email")
    public User getUserByEmail(@RequestParam String email) {
        return userRepository.findByEmailIgnoreCase(email).orElseThrow();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @Valid @RequestBody User updated) {
        ValidationUtil.assureIdConsistent(updated, id);
        userRepository.save(updated);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        ValidationUtil.checkNew(user);
        User created = userRepository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(CURRENT_URL + "/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        userRepository.deleteById(id);
    }

    @GetMapping("/vote") //return all votes by default
    public List<Vote> getVotes(@RequestParam @Nullable LocalDate startDate,
                               @RequestParam @Nullable LocalDate endDate,
                               @RequestParam  @Nullable List<Integer> users,
                               @RequestParam  @Nullable List<Integer> restaurants) {
        startDate = DateUtil.checkedStartDateOrMin(startDate);
        endDate = DateUtil.checkedEndDate(endDate);
        List<Integer> userIds = users == null ? Collections.emptyList():users;
        List<Integer> restaurantIds = restaurants == null ? Collections.emptyList():restaurants;
//        List<Integer> userIds = users == null ? Collections.emptyList()
//                : users.stream().map(BaseEntity::getId).toList();
//        List<Integer> restaurantIds = restaurants == null ? Collections.emptyList()
//                : restaurants.stream().map(BaseEntity::getId).toList();
        if (userIds.isEmpty() && restaurantIds.isEmpty()) {
            return voteRepository.getByDate(startDate, endDate);
        } else if (userIds.isEmpty()) {
            return voteRepository.getByDateAndRestaurant(restaurantIds, startDate, endDate);
        } else if (restaurantIds.isEmpty()) {
            return voteRepository.getByDateAndUser(userIds, startDate, endDate);
        } else {
            return voteRepository.getCustom(userIds, restaurantIds, startDate, endDate);
        }
    }

    @GetMapping("/vote_count")
    public List<RestaurantTo> getVoteCount(@RequestParam @Nullable LocalDate startDate,
                                           @RequestParam @Nullable LocalDate endDate) {
        return voteRepository.getVoteCountByRestaurant(DateUtil.checkedStartDateOrToday(startDate),
                DateUtil.checkedEndDate(endDate));
    }
}

