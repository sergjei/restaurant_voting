package topjava.restaurantvoting.controller;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import topjava.restaurantvoting.DateUtils;
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
        User user = new User();
        user.setId(1);
        return authUser.getUser();
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody User updated) {
        User user = updated;
        user.setId(1);
        updated.setId(1);
        userRepository.save(updated);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> create(@RequestBody User user) {
        user.setRoles(Set.of(Role.USER));
        User created = userRepository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(CURRENT_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        userRepository.deleteById(4);
    }

    @GetMapping("/menu")
    public List<Restaurant> getMenu() {
        return restaurantRepository.getTodayMenu();
    }

    @GetMapping("/vote")
    public List<Vote> getVotes(User user, @RequestParam @Nullable LocalDate startDate, @RequestParam @Nullable LocalDate endDate) {
        List<Vote> f = voteRepository.getByDateAndUser(1,
                DateUtils.checkedStartDate(startDate),
                DateUtils.checkedEndDate(endDate));
        return f;
    }

    @PostMapping(value = "/vote", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Vote> vote(@RequestBody Vote vote, User user) {
        Vote current = voteRepository.getTodayByUser(1);
        if (vote.getVoteDate().isEqual(LocalDate.now()) &&
                (LocalTime.now().isBefore(LocalTime.of(22, 00, 00))||(current == null))
        ) {
            Integer id = current == null? null:current.getId();
            vote.setId(id);
            Vote actual = voteRepository.save(vote);
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(CURRENT_URL + "/vote/{id}")
                    .buildAndExpand(actual.getId()).toUri();
            return ResponseEntity.created(uriOfNewResource).body(actual);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_MODIFIED).body(current);
        }
    }

//    @GetMapping("/users")
//    public List<User> getUsers(){
//        return userRepository.findAll();
//    }
//
//    @GetMapping("/user_by_email")
//    public User getUserByEmail(@RequestParam String email){
//        return userRepository.getByEmail(email);
//    }
}
