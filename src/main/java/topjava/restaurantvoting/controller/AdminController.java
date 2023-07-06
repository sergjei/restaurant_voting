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
import topjava.restaurantvoting.model.User;
import topjava.restaurantvoting.repository.UserRepository;
import topjava.restaurantvoting.repository.VoteRepository;
import topjava.restaurantvoting.to.RestaurantTo;
import topjava.restaurantvoting.utils.DateUtil;
import topjava.restaurantvoting.utils.ValidationUtil;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(value = AdminController.CURRENT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "basicAuth")
public class AdminController {
    public static final String CURRENT_URL = "/rest/admin";
    public UserRepository userRepository;
    public VoteRepository voteRepository;

    public AdminController(UserRepository userRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    @GetMapping("/users")
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public User get(@PathVariable Integer id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find user with  id = " + id));
    }

    @GetMapping("/users/by_email")
    public User getUserByEmail(@RequestParam String email) {
        return userRepository.findByEmailIgnoreCase(email).orElseThrow(
                () -> new EntityNotFoundException("Can`t find user with  email = " + email));
    }

    @PutMapping(value = "/users/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer id, @Valid @RequestBody User updated) {
        ValidationUtil.assureIdConsistent(updated, id);
        userRepository.save(updated);
    }

    @PostMapping(value = "/users", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> create(@Valid @RequestBody User user) {
        ValidationUtil.checkNew(user);
        User created = userRepository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(CURRENT_URL + "/users/{id}").buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        userRepository.deleteById(id);
    }

    @GetMapping("/votes_count/today")
    public List<RestaurantTo> getTodayVotes() {
        return voteRepository.getVoteCountByRestaurant(DateUtil.getToday(), DateUtil.getToday());
    }

    @GetMapping("/votes_count")
    public List<RestaurantTo> getVoteCount(@RequestParam(value = "startDate") @Nullable LocalDate startDate,
                                           @RequestParam(value = "endDate") @Nullable LocalDate endDate) {
        return voteRepository.getVoteCountByRestaurant(DateUtil.checkedStartDateOrMin(startDate),
                DateUtil.checkedEndDate(endDate));
    }
}

