package com.github.sergjei.restaurant_voting.controller;

import com.github.sergjei.restaurant_voting.RestaurantTestData;
import com.github.sergjei.restaurant_voting.repository.RestaurantRepository;
import com.github.sergjei.restaurant_voting.to.RestaurantTo;
import com.github.sergjei.restaurant_voting.utils.MenuItemUtil;
import com.github.sergjei.restaurant_voting.utils.RestaurantsUtil;
import com.github.sergjei.restaurant_voting.utils.json.JsonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static com.github.sergjei.restaurant_voting.MenuItemTestData.*;
import static com.github.sergjei.restaurant_voting.RestaurantTestData.*;
import static com.github.sergjei.restaurant_voting.UserTestData.ADMIN_EMAIL;
import static com.github.sergjei.restaurant_voting.UserTestData.USER_EMAIL;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class RestaurantControllerTest extends AbstractControllerTest {
    public static final String CURRENT_URL = "/rest/restaurants";
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getAll() throws Exception {
        List<RestaurantTo> expected = RestaurantsUtil.getListTos(List.of(RESTAURANT_1, RESTAURANT_2));
        perform(MockMvcRequestBuilders.get(CURRENT_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(expected));
    }

    @Test
    void getUnauthorized() throws Exception {
        perform(MockMvcRequestBuilders.get(CURRENT_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(CURRENT_URL + "/{id}", RESTAURANT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(RestaurantsUtil.createToFrom(RESTAURANT_1)));
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getMenu() throws Exception {
        RestaurantTestData.setTodayMenu();
        ResultActions action = perform(MockMvcRequestBuilders.get(CURRENT_URL + "/todaymenu"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(RestaurantsUtil.getListTos(List.of(RestaurantTestData.RESTAURANT_1, RESTAURANT_2))));
        List<RestaurantTo> restaurants = JsonUtil.readValues(action.andReturn().getResponse().getContentAsString(), RestaurantTo.class);
        MENU_ITEM_TO_MATCHER.assertMatch(restaurants.get(0).getMenu(), MenuItemUtil.getListTos(R1_MENU_TODAY));
        MENU_ITEM_TO_MATCHER.assertMatch(restaurants.get(1).getMenu(), MenuItemUtil.getListTos(R2_MENU_TODAY));
    }
}