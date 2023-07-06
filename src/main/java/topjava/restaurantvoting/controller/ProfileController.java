package topjava.restaurantvoting.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
import topjava.restaurantvoting.to.RestaurantTo;
import topjava.restaurantvoting.to.VoteTo;
import topjava.restaurantvoting.utils.DateUtil;
import topjava.restaurantvoting.utils.RestaurantsUtil;
import topjava.restaurantvoting.utils.ValidationUtil;
import topjava.restaurantvoting.utils.VotesUtil;
import topjava.restaurantvoting.utils.exception.IllegalRequestTimeException;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping(value = ProfileController.CURRENT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "basicAuth")
public class ProfileController {

    public static final String CURRENT_URL = "/rest/profile";
    public UserRepository userRepository;
    public RestaurantRepository restaurantRepository;
    public VoteRepository voteRepository;

    public ProfileController(UserRepository userRepository, RestaurantRepository restaurantRepository, VoteRepository voteRepository) {
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
    public List<RestaurantTo> getMenu() {
        List<Restaurant> origin = restaurantRepository.getTodayMenu();
        return RestaurantsUtil.getListTos(origin);
    }

    @GetMapping("/votes")
    public List<VoteTo> getVotes(@AuthenticationPrincipal AuthUser authUser,
                                 @RequestParam @Nullable LocalDate startDate,
                                 @RequestParam @Nullable LocalDate endDate) {
        return VotesUtil.getListTos(voteRepository.getByDateAndUser(List.of(authUser.id()),
                DateUtil.checkedStartDateOrMin(startDate),
                DateUtil.checkedEndDate(endDate)));
    }

    @PostMapping(value = "/votes")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VoteTo> vote(@AuthenticationPrincipal AuthUser authUser,
                                       @RequestParam(value = "restaurantId") Integer restaurantId) {
        Vote vote = new Vote(null, LocalDate.now(), authUser.getUser(), null);
        vote.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find restaurant with  id = " + restaurantId)
        ));
        Vote actual = voteRepository.save(vote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(CURRENT_URL + "/votes/{id}")
                .buildAndExpand(actual.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(VotesUtil.createToFrom(actual));
    }

    @PutMapping(value = "/votes/{id}")
    public ResponseEntity<VoteTo> changeVote(@AuthenticationPrincipal AuthUser authUser,
                                             @PathVariable Integer id, @RequestParam(value = "restaurantId") Integer restaurantId) {
        Vote current = voteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find vote with  id = " + id)
        );
        if (!Objects.equals(current.getVoteDate(), LocalDate.now())) {
            throw new IllegalRequestTimeException("Can`t change not today vote!");
        }
        Vote vote = new Vote(current.getId(), LocalDate.now(), authUser.getUser(),
                restaurantRepository.findById(restaurantId).orElseThrow(
                        () -> new EntityNotFoundException("Can`t find restaurant with  id = " + restaurantId)
                ));
        if (LocalTime.now().isBefore(LocalTime.of(11, 0, 0))) {
            Vote actual = voteRepository.save(vote);
            return ResponseEntity.ok(VotesUtil.createToFrom(actual));
        }
        throw new IllegalRequestTimeException("Vote can be changed only before 11 AM");
    }
}
