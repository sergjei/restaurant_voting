package com.github.sergjei.restaurant_voting.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.sergjei.restaurant_voting.repository.VoteRepository;
import com.github.sergjei.restaurant_voting.to.VoteTo;
import com.github.sergjei.restaurant_voting.utils.DateUtil;
import com.github.sergjei.restaurant_voting.utils.VotesUtil;
import com.github.sergjei.restaurant_voting.utils.json.JsonUtil;

import java.time.Clock;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.sergjei.restaurant_voting.RestaurantTestData.RESTAURANT_ID;
import static com.github.sergjei.restaurant_voting.RestaurantTestData.setVotesRest;
import static com.github.sergjei.restaurant_voting.UserTestData.*;
import static com.github.sergjei.restaurant_voting.VoteTestData.*;

class ProfileVoteControllerTest extends AbstractControllerTest {
    public static final String CURRENT_URL = "/rest/profile";
    @Autowired
    private VoteRepository voteRepository;

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getVotes() throws Exception {
        setVotes();
        setVotesRest();
        ResultActions action = perform(MockMvcRequestBuilders.get(CURRENT_URL + "/votes"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        List<VoteTo> actual = JsonUtil.readValues(action.andReturn().getResponse().getContentAsString(), VoteTo.class);
        assertEquals(1, actual.size());
        VOTE_TO_MATCHER.assertMatch(actual, VotesUtil.createToFrom(VOTE_2));
        assertEquals(actual.get(0).getRestaurantId(), RESTAURANT_ID + 1);
        assertEquals(actual.get(0).getUserId(), ADMIN_ID);
    }

    @Test
    @WithUserDetails(value = USER_2_EMAIL)
    void vote() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.post(CURRENT_URL + "/votes")
                .param("restaurantId", "1"))
                .andDo(print())
                .andExpect(status().isCreated());
        VoteTo created = VOTE_TO_MATCHER.readFromJson(action);
        VoteTo newOne = VotesUtil.createToFrom(getNewVote());
        newOne.setId(created.getId());
        VOTE_TO_MATCHER.assertMatch(created, newOne);
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void changeVote() throws Exception {
        Clock fixedClock = Clock.fixed(LocalTime.of(10, 59, 59).atDate(DateUtil.TODAY).atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        DateUtil.setClock(fixedClock);
        VoteTo origin = VotesUtil.createToFrom(voteRepository.findById(VOTE_ID + 3).orElseThrow(
                () -> new EntityNotFoundException("Can`t find vote with  id = " + VOTE_ID + 3)
        ));
        ResultActions actionUpdate = perform(MockMvcRequestBuilders.put(CURRENT_URL + "/votes")
                .param("restaurantId", "2"))
                .andDo(print());
        VoteTo updated = VOTE_TO_MATCHER.readFromJson(actionUpdate);
        origin.setRestaurantId(2);
        VOTE_TO_MATCHER.assertMatch(updated, origin);
        DateUtil.initDefaultClock();
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void changeVoteAfterEleven() throws Exception {
        Clock fixedClock = Clock.fixed(LocalTime.of(11, 0, 0).atDate(DateUtil.TODAY).atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault());
        DateUtil.setClock(fixedClock);
        perform(MockMvcRequestBuilders.put(CURRENT_URL + "/votes")
                .param("restaurantId", "2"))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(result -> assertEquals("422 UNPROCESSABLE_ENTITY \"Vote can be changed only before 11 AM\"", result.getResolvedException().getMessage()));
        DateUtil.initDefaultClock();
    }
}