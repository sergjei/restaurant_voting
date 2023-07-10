package topjava.restaurantvoting.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import topjava.restaurantvoting.repository.VoteRepository;
import topjava.restaurantvoting.to.RestaurantTo;
import topjava.restaurantvoting.utils.DateUtil;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping(value = AdminVoteController.CURRENT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "basicAuth")
@Transactional
public class AdminVoteController {
    public static final String CURRENT_URL = "/rest/admin";
    public VoteRepository voteRepository;

    public AdminVoteController(VoteRepository voteRepository) {
        this.voteRepository = voteRepository;
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

