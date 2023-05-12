package topjava.restaurantvoting.controller;

import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import topjava.restaurantvoting.model.*;
import topjava.restaurantvoting.repository.RestaurantRepository;
import topjava.restaurantvoting.repository.UserRepository;
import topjava.restaurantvoting.repository.VoteRepository;
import topjava.restaurantvoting.utils.DateUtil;
import topjava.restaurantvoting.utils.ValidationUtil;

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

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal AuthUser authUser) {
        userRepository.deleteById(authUser.id());
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@AuthenticationPrincipal AuthUser authUser,
                       @Valid @RequestBody User updated) {
        User oldUser = authUser.getUser();
        ValidationUtil.assureIdConsistent(updated, oldUser.id());
        if (updated.getPassword() == null) {
            updated.setPassword(oldUser.getPassword());
        }
        userRepository.save(updated);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@Valid @RequestBody User user) {
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
                               @RequestParam @Nullable LocalDate startDate,
                               @RequestParam @Nullable LocalDate endDate) {
        return voteRepository.getByDateAndUser(List.of(authUser.id()), DateUtil.checkedStartDateOrMin(startDate),
                DateUtil.checkedEndDate(endDate));
    }

    @PostMapping(value = "/vote")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Vote> vote(@AuthenticationPrincipal AuthUser authUser,
                                     @RequestParam(value = "restId") Integer restId) {
        Vote vote = new Vote(null, LocalDate.now(), authUser.getUser(), null);
        Vote current = voteRepository.getTodayByUser(authUser.id());
        if (current == null) {
            vote.setRestaurant(restaurantRepository.findById(restId).orElseThrow(
                    () -> new EntityNotFoundException("Can`t find restaurant with  id = " + restId)
            ));
            Vote actual = voteRepository.save(vote);
            URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path(CURRENT_URL + "/vote/{id}")
                    .buildAndExpand(actual.getId()).toUri();
            return ResponseEntity.created(uriOfNewResource).body(actual);
        } else {
            return changeVote(authUser, current.getId(), current.getRestaurant().getId());
        }
    }

    @PutMapping(value = "/vote/{id}")
    public ResponseEntity<Vote> changeVote(@AuthenticationPrincipal AuthUser authUser,
                                           @PathVariable Integer id, @RequestParam(value = "restId") Integer restId) {
        Vote current = voteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find vote with  id = " + id)
        );
        Vote vote = new Vote(current.getId(), LocalDate.now(), authUser.getUser(),
                restaurantRepository.findById(restId).orElseThrow(
                        () -> new EntityNotFoundException("Can`t find restaurant with  id = " + restId)
                ));
        if (LocalTime.now().isBefore(LocalTime.of(11, 0, 0))) {
            Vote actual = voteRepository.save(vote);
            return ResponseEntity.ok(actual);
        }
        return ResponseEntity.status(HttpStatus.LOCKED).body(current);
    }
}
