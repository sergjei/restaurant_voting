package com.github.sergjei.restaurant_voting.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import com.github.sergjei.restaurant_voting.RestaurantTestData;
import com.github.sergjei.restaurant_voting.model.User;
import com.github.sergjei.restaurant_voting.repository.UserRepository;
import com.github.sergjei.restaurant_voting.to.RestaurantTo;
import com.github.sergjei.restaurant_voting.utils.MenuItemUtil;
import com.github.sergjei.restaurant_voting.utils.RestaurantsUtil;
import com.github.sergjei.restaurant_voting.utils.json.JsonUtil;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.sergjei.restaurant_voting.MenuItemTestData.*;
import static com.github.sergjei.restaurant_voting.RestaurantTestData.RESTAURANT_2;
import static com.github.sergjei.restaurant_voting.RestaurantTestData.RESTAURANT_TO_MATCHER;
import static com.github.sergjei.restaurant_voting.UserTestData.*;

class ProfileControllerTest extends AbstractControllerTest {
    public static final String CURRENT_URL = "/rest/profile";
    @Autowired
    private UserRepository userRepository;

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(CURRENT_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(USER));
    }

    @Test
    void getUnautherized() throws Exception {
        perform(MockMvcRequestBuilders.get(CURRENT_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(CURRENT_URL))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userRepository.findAll(), List.of(ADMIN, USER_2));
    }

    @Test
    @WithUserDetails(value = USER_2_EMAIL)
    void update() throws Exception {
        User updated = getUpdated(userRepository.findByEmailIgnoreCase(USER_2_EMAIL).orElseThrow(
                () -> new EntityNotFoundException("Can`t find user with  email = " + USER_2_EMAIL)
        ));
        String json = JsonUtil.writeAdditionProp(updated, "password", "updatedPass");
        perform(MockMvcRequestBuilders.put(CURRENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userRepository.findByEmailIgnoreCase(updated.getEmail()).orElseThrow(
                () -> new EntityNotFoundException("Can`t find user with  email = " + updated.getEmail())
        ), updated);
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void updateInvalid() throws Exception {
        User updated = new User(null, "serg", "plot", "admin@gmail.com", "");
        perform(MockMvcRequestBuilders.put(CURRENT_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void register() throws Exception {
        User newOne = getNew();
        String json = JsonUtil.writeAdditionProp(newOne, "password", "newPass");
        ResultActions action = perform(MockMvcRequestBuilders.post(CURRENT_URL + "/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated());
        User created = USER_MATCHER.readFromJson(action);
        newOne.setId(created.getId());
        USER_MATCHER.assertMatch(created, newOne);
        USER_MATCHER.assertMatch(userRepository.findById(created.getId()).orElseThrow(
                () -> new EntityNotFoundException("Can`t find user with  id = " + created.getId())
        ), newOne);
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void getMenu() throws Exception {
        RestaurantTestData.setTodayMenu();
        ResultActions action = perform(MockMvcRequestBuilders.get(CURRENT_URL + "/menu"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_TO_MATCHER.contentJson(RestaurantsUtil.getListTos(List.of(RestaurantTestData.RESTAURANT_1, RESTAURANT_2))));
        List<RestaurantTo> restaurants = JsonUtil.readValues(action.andReturn().getResponse().getContentAsString(), RestaurantTo.class);
        MENU_ITEM_TO_MATCHER.assertMatch(restaurants.get(0).getMenu(), MenuItemUtil.getListTos(R1_MENU_TODAY));
        MENU_ITEM_TO_MATCHER.assertMatch(restaurants.get(1).getMenu(), MenuItemUtil.getListTos(R2_MENU_TODAY));
    }
}