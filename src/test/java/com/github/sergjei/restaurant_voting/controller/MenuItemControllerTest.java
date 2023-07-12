package com.github.sergjei.restaurant_voting.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import com.github.sergjei.restaurant_voting.repository.MenuItemRepository;
import com.github.sergjei.restaurant_voting.to.MenuItemTo;
import com.github.sergjei.restaurant_voting.utils.DateUtil;
import com.github.sergjei.restaurant_voting.utils.MenuItemUtil;
import com.github.sergjei.restaurant_voting.utils.json.JsonUtil;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static com.github.sergjei.restaurant_voting.MenuItemTestData.*;
import static com.github.sergjei.restaurant_voting.RestaurantTestData.RESTAURANT_ID;
import static com.github.sergjei.restaurant_voting.UserTestData.ADMIN_EMAIL;

class MenuItemControllerTest extends AbstractControllerTest {
    public static final String CURRENT_URL = "/rest/admin/restaurants/{restaurant_id}/menu_items";
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(CURRENT_URL, RESTAURANT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_TO_MATCHER.contentJson(MenuItemUtil.getListTos(R1_MENU_ALL)));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getToday() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("startDate", DateUtil.getToday().toString());
        parameters.add("endDate", DateUtil.getToday().toString());
        perform(MockMvcRequestBuilders.get(CURRENT_URL, RESTAURANT_ID)
                .params(parameters))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_TO_MATCHER.contentJson(MenuItemUtil.getListTos(R1_MENU_TODAY)));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(CURRENT_URL + "/{id}", RESTAURANT_ID, MENU_ITEM_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_TO_MATCHER.contentJson(MenuItemUtil.createToFrom(MENU_ITEM_1_R_1_YSTRD)));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(CURRENT_URL + "/{id}", RESTAURANT_ID, MENU_ITEM_ID + 1))
                .andExpect(status().isNoContent());
        MENU_ITEM_MATCHER.assertMatch(
                menuItemRepository.getByRestaurantAndDateInclusive(RESTAURANT_ID, DateUtil.getToday().minusDays(1), DateUtil.getToday()),
                List.of(MENU_ITEM_1_R_1_YSTRD, MENU_ITEM_3_R_1_YSTRD, MENU_ITEM_4_R_1_TODAY, MENU_ITEM_5_R_1_TODAY, MENU_ITEM_6_R_1_TODAY)
        );
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void update() throws Exception {
        MenuItemTo updated = MenuItemUtil.createToFrom(getUpdatedMenuItem(menuItemRepository.findById(MENU_ITEM_ID).orElseThrow(
                () -> new EntityNotFoundException("Can`t find menu item with  id = " + MENU_ITEM_ID)
        )));
        perform(MockMvcRequestBuilders.put(CURRENT_URL + "/{id}", RESTAURANT_ID, MENU_ITEM_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MENU_ITEM_TO_MATCHER.assertMatch(MenuItemUtil.createToFrom(menuItemRepository.findById(MENU_ITEM_ID).orElseThrow(
                () -> new EntityNotFoundException("Can`t find menu item with  id = " + MENU_ITEM_ID)
        )), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void createOne() throws Exception {
        MenuItemTo newOne = MenuItemUtil.createToFrom(getNewMenuItem());
        ResultActions action = perform(MockMvcRequestBuilders.post(CURRENT_URL, RESTAURANT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newOne)))
                .andExpect(status().isCreated())
                .andDo(print());
        MenuItemTo created = MENU_ITEM_TO_MATCHER.readFromJson(action);
        int id = created.getId();
        newOne.setId(id);
        MENU_ITEM_TO_MATCHER.assertMatch(created, newOne);
        MENU_ITEM_TO_MATCHER.assertMatch(MenuItemUtil.createToFrom(menuItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find menu item with  id = " + id)
        )), newOne);
    }
}