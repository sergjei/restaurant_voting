package com.github.sergjei.restaurant_voting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.github.sergjei.restaurant_voting.model.Restaurant;
import com.github.sergjei.restaurant_voting.to.RestaurantTo;
import com.github.sergjei.restaurant_voting.utils.DateUtil;
import com.github.sergjei.restaurant_voting.utils.RestaurantsUtil;
import com.github.sergjei.restaurant_voting.utils.json.JsonUtil;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.sergjei.restaurant_voting.RestaurantTestData.*;
import static com.github.sergjei.restaurant_voting.UserTestData.ADMIN_EMAIL;

class AdminVoteControllerTest extends AbstractControllerTest {
    public static final String CURRENT_URL = "/rest/admin";

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getVoteCount() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("startDate", DateUtil.getToday().minusDays(1).toString());
        parameters.add("endDate", DateUtil.getToday().minusDays(1).toString());
        ResultActions action = perform(MockMvcRequestBuilders.get(CURRENT_URL + "/votes_count")
                .params(parameters))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        List<RestaurantTo> restaurantTos = JsonUtil.readValues(action.andReturn().getResponse().getContentAsString(), RestaurantTo.class);
        assertEquals(2, restaurantTos.size());
        assertEquals(1, restaurantTos.get(0).getVoteCount());
        assertEquals(2, restaurantTos.get(1).getVoteCount());
        Restaurant firstRest = RestaurantsUtil.createFromTo(restaurantTos.get(0));
        Restaurant secondRest = RestaurantsUtil.createFromTo(restaurantTos.get(1));
        RESTAURANT_MATCHER.assertMatch(firstRest, RESTAURANT_1);
        RESTAURANT_MATCHER.assertMatch(secondRest, RESTAURANT_2);
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getTodayVotes() throws Exception {
        ResultActions action = perform(MockMvcRequestBuilders.get(CURRENT_URL + "/votes_count/today"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        List<RestaurantTo> restaurantTos = RESTAURANT_TO_MATCHER.readFromJsonList(action);
        assertEquals(2, restaurantTos.size());
        assertEquals(1, restaurantTos.get(0).getVoteCount());
        assertEquals(0, restaurantTos.get(1).getVoteCount());
        Restaurant firstRest = RestaurantsUtil.createFromTo(restaurantTos.get(0));
        Restaurant secondRest = RestaurantsUtil.createFromTo(restaurantTos.get(1));
        RESTAURANT_MATCHER.assertMatch(firstRest, RESTAURANT_1);
        RESTAURANT_MATCHER.assertMatch(secondRest, RESTAURANT_2);
    }
}