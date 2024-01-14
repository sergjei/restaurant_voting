package com.github.sergjei.restaurant_voting.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.sergjei.restaurant_voting.repository.RestaurantRepository;
import com.github.sergjei.restaurant_voting.to.RestaurantTo;
import com.github.sergjei.restaurant_voting.utils.RestaurantsUtil;
import com.github.sergjei.restaurant_voting.utils.json.JsonUtil;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.sergjei.restaurant_voting.RestaurantTestData.*;
import static com.github.sergjei.restaurant_voting.UserTestData.ADMIN_EMAIL;

class AdminRestaurantControllerTest extends AbstractControllerTest {
    public static final String CURRENT_URL = "/rest/admin/restaurants";
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
    @WithUserDetails(value = ADMIN_EMAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(CURRENT_URL + "/{id}", RESTAURANT_ID))
                .andExpect(status().isNoContent());
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.findAll(), RESTAURANT_2);
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void update() throws Exception {
        RestaurantTo updated = RestaurantsUtil.createToFrom(getUpdatedRestaurant(restaurantRepository.findById(RESTAURANT_ID + 1).orElseThrow(
                () -> new EntityNotFoundException("Can`t find restaurant with  id = " + (RESTAURANT_ID + 1))
        )));
        perform(MockMvcRequestBuilders.put(CURRENT_URL + "/{id}", RESTAURANT_ID + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        RESTAURANT_TO_MATCHER.assertMatch(RestaurantsUtil.createToFrom(restaurantRepository.findById(RESTAURANT_ID + 1).orElseThrow(
                () -> new EntityNotFoundException("Can`t find restaurant with  id = " + (RESTAURANT_ID + 1))
        )), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void create() throws Exception {
        RestaurantTo newOne = RestaurantsUtil.createToFrom(getNewRestaurant());
        ResultActions action = perform(MockMvcRequestBuilders.post(CURRENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newOne)))
                .andExpect(status().isCreated())
                .andDo(print());
        RestaurantTo created = RESTAURANT_TO_MATCHER.readFromJson(action);
        newOne.setId(created.getId());
        RESTAURANT_TO_MATCHER.assertMatch(created, newOne);
        RESTAURANT_TO_MATCHER.assertMatch(RestaurantsUtil.createToFrom(restaurantRepository.findById(created.getId()).orElseThrow(
                () -> new EntityNotFoundException("Can`t find restaurant with  id = " + created.getId())
        )), newOne);
    }
}