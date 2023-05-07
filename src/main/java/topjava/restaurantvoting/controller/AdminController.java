package topjava.restaurantvoting.controller;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import topjava.restaurantvoting.DateUtils;
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
import java.util.ArrayList;
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
    public List<User> getAll(){
        return  userRepository.findAll();
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Integer id){
        return userRepository.findById(id).orElseThrow();
    }
        @GetMapping("/user_by_email")
    public User getUserByEmail(@RequestParam String email){
        return userRepository.findByEmailIgnoreCase(email).orElseThrow();
    }
    @PutMapping(value ="/{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Integer userId,@RequestBody User updated) {
        updated.setId(userId);
        userRepository.save(updated);
    }

    @PostMapping(value ="/{id}",consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> create(@PathVariable Integer userId,@RequestBody User user) {
        User created = userRepository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(CURRENT_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer userId) {
        userRepository.deleteById(userId);
    }

    @GetMapping("/vote")
    public List<Vote> getVotes(@RequestParam @Nullable LocalDate startDate, @RequestParam @Nullable LocalDate endDate,
                               @RequestParam @Nullable List<User> users, @RequestParam @Nullable List<Restaurant> restaurants){
            List<Integer> userIds = users==null?null:users.stream().map(BaseEntity::getId).toList();
            List<Integer> restaurantIds = restaurants==null?null:restaurants.stream().map(BaseEntity::getId).toList();
        userIds= new ArrayList<>();
        restaurantIds= new ArrayList<>();
            userIds.add(1);
            restaurantIds.add(1);

//            if(userIds.isEmpty()&&restaurantIds.isEmpty()){
//                return voteRepository.getByDate(checkedStartDate(startDate),checkedEndDate(endDate));
//            }
//            else if(restaurantIds.isEmpty()){
//                return voteRepository.getByDateAndUser()
//            }
            List<Vote> f = voteRepository.getCustom(userIds,restaurantIds, DateUtils.checkedStartDate(startDate),DateUtils.checkedEndDate(endDate));
            return f;
    }
    @GetMapping("/vote_count")
    public List<RestaurantTo> getVoteCount(@RequestParam @Nullable LocalDate startDate,
                                           @RequestParam @Nullable LocalDate endDate){

        List<RestaurantTo> t = voteRepository.getVoteCountByRestaurant(DateUtils.checkedStartDateForCount(startDate),
                DateUtils.checkedEndDate(endDate));
        return t;
    }

}

