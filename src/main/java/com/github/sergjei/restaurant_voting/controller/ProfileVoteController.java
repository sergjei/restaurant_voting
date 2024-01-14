package com.github.sergjei.restaurant_voting.controller;

import com.github.sergjei.restaurant_voting.model.AuthUser;
import com.github.sergjei.restaurant_voting.model.Vote;
import com.github.sergjei.restaurant_voting.repository.RestaurantRepository;
import com.github.sergjei.restaurant_voting.repository.VoteRepository;
import com.github.sergjei.restaurant_voting.to.VoteTo;
import com.github.sergjei.restaurant_voting.utils.DateUtil;
import com.github.sergjei.restaurant_voting.utils.VotesUtil;
import com.github.sergjei.restaurant_voting.utils.exception.IllegalRequestDataException;
import com.github.sergjei.restaurant_voting.utils.exception.IllegalRequestTimeException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping(value = ProfileVoteController.CURRENT_URL, produces = MediaType.APPLICATION_JSON_VALUE)
@SecurityRequirement(name = "basicAuth")
@Transactional
public class ProfileVoteController {

    public static final String CURRENT_URL = "/rest/profile";
    public RestaurantRepository restaurantRepository;
    public VoteRepository voteRepository;

    public ProfileVoteController(RestaurantRepository restaurantRepository, VoteRepository voteRepository) {
        this.restaurantRepository = restaurantRepository;
        this.voteRepository = voteRepository;
    }

    @GetMapping("/votes")
    public List<VoteTo> getVotes(@AuthenticationPrincipal AuthUser authUser,
                                 @RequestParam @Nullable LocalDate startDate,
                                 @RequestParam @Nullable LocalDate endDate) {
        return VotesUtil.getListTos(voteRepository.getByDateAndUser(authUser.id(),
                DateUtil.checkedStartDateOrMin(startDate),
                DateUtil.checkedEndDate(endDate)));
    }

    @PostMapping(value = "/votes")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<VoteTo> vote(@AuthenticationPrincipal AuthUser authUser,
                                       @RequestParam(value = "restaurantId") Integer restaurantId) {
        Vote vote = new Vote(null, DateUtil.getToday(), authUser.getUser(), null);
        vote.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find restaurant with  id = " + restaurantId)
        ));
        Vote actual = voteRepository.save(vote);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(CURRENT_URL + "/votes/{id}")
                .buildAndExpand(actual.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(VotesUtil.createToFrom(actual));
    }

    @PutMapping(value = "/votes")
    public ResponseEntity<VoteTo> changeVote(@AuthenticationPrincipal AuthUser authUser,
                                             @RequestParam(value = "restaurantId") Integer restaurantId) {
        Vote current = voteRepository.getByDateAndUserForOneDay(authUser.id(), DateUtil.getToday()).orElseThrow(
                () -> new EntityNotFoundException("Can`t find today`s vote! ")
        );
        if (!Objects.equals(current.getVoteDate(), DateUtil.getToday())) {
            throw new IllegalRequestTimeException("Can`t change not today vote!");
        }
        Vote vote = new Vote(current.getId(), DateUtil.getToday(), authUser.getUser(),
                restaurantRepository.findById(restaurantId).orElseThrow(
                        () -> new EntityNotFoundException("Can`t find restaurant with  id = " + restaurantId)
                ));
        if (LocalTime.now(DateUtil.getClock()).isBefore(DateUtil.CHANGE_VOTE_END)) {
            Vote actual = voteRepository.save(vote);
            return ResponseEntity.ok(VotesUtil.createToFrom(actual));
        }
        throw new IllegalRequestDataException("Vote can be changed only before 11 AM");
    }
}
