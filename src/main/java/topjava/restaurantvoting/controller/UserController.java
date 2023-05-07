package topjava.restaurantvoting.controller;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import topjava.restaurantvoting.DateUtil;
import topjava.restaurantvoting.ValidationUtil;
import topjava.restaurantvoting.model.*;
import topjava.restaurantvoting.repository.RestaurantRepository;
import topjava.restaurantvoting.repository.UserRepository;
import topjava.restaurantvoting.repository.VoteRepository;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(value = UserController.CURRENT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    public static final String CURRENT_URL = "/rest/profile";
    public UserRepository userRepository;
    public RestaurantRepository restaurantRepository;
    public VoteRepository voteRepository;

    public UserController(UserRepository userRepository, RestaurantRepository restaurantRepository, VoteRepository voteRepository) {
        this.userRepository = userRepository;
        this.restaurantRepository = restaurantRepository;
        this.voteRepository = voteRepository;
    }

    @GetMapping
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        return authUser.getUser();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser, @RequestBody User updated) {
        User oldUser = authUser.getUser();
        ValidationUtil.assureIdConsistent(updated, oldUser.id());
        if (updated.getPassword() == null) {
            updated.setPassword(oldUser.getPassword());
        }
        userRepository.save(updated);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> create(@RequestBody User user) {
        ValidationUtil.checkNew(user);
        user.setRoles(Set.of(Role.USER));
        User created = userRepository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(CURRENT_URL + "/register/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @GetMapping("/menu")
    public List<Restaurant> getMenu() {
        return restaurantRepository.getTodayMenu();
    }

    @GetMapping("/vote")
    public List<Vote> getVotes(@AuthenticationPrincipal AuthUser authUser,
                               @RequestParam @Nullable LocalDate startDate, @RequestParam @Nullable LocalDate endDate) {
        return voteRepository.getByDateAndUser(List.of(authUser.id()), DateUtil.checkedStartDateOrMin(startDate),
                DateUtil.checkedEndDate(endDate));
    }

    @PostMapping(value = "/vote", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> vote(@AuthenticationPrincipal AuthUser authUser, @RequestBody Vote vote) {
        ValidationUtil.checkNew(vote);
        ValidationUtil.assureIdConsistent(vote.getUser(), authUser.id());
        ValidationUtil.assureVoteDate(vote);
        Vote current = voteRepository.getTodayByUser(authUser.id());
        if (current == null) {
            Vote actual = voteRepository.save(vote);
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(CURRENT_URL + "/vote/{id}")
                    .buildAndExpand(actual.getId()).toUri();
            return ResponseEntity.created(uriOfNewResource).body(actual);
        } else {
            vote.setId(current.getId());
            return changeVote(authUser, vote);
        }
    }

    @PutMapping(value = "/vote", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> changeVote(@AuthenticationPrincipal AuthUser authUser, @RequestBody Vote vote) {
        ValidationUtil.assureIdConsistent(vote.getUser(), authUser.id());
        ValidationUtil.assureVoteDate(vote);
        if (LocalTime.now().isBefore(LocalTime.of(11, 0, 0))) {
            Vote actual = voteRepository.save(vote);
            return ResponseEntity.ok(actual);
        }
        throw new IllegalStateException("Too late to change vote!");
    }
}
