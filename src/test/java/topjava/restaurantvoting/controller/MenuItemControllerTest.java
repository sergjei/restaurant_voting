package topjava.restaurantvoting.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import topjava.restaurantvoting.model.MenuItem;
import topjava.restaurantvoting.repository.MenuItemRepository;
import topjava.restaurantvoting.to.MenuItemTo;
import topjava.restaurantvoting.utils.MenuItemUtil;
import topjava.restaurantvoting.utils.json.JsonUtil;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static topjava.restaurantvoting.MenuItemTestData.*;
import static topjava.restaurantvoting.RestaurantTestData.RESTAURANT_ID;
import static topjava.restaurantvoting.UserTestData.ADMIN_EMAIL;

class MenuItemControllerTest extends AbstractControllerTest {
    public static final String CURRENT_URL = "/rest/admin/restaurants/{restaurant_id}/meals";
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(CURRENT_URL, RESTAURANT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(R1_MENU_ALL));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getToday() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("startDate", LocalDate.now().toString());
        parameters.add("endDate", LocalDate.now().toString());
        perform(MockMvcRequestBuilders.get(CURRENT_URL, RESTAURANT_ID)
                .params(parameters))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(R1_MENU_TODAY));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(CURRENT_URL + "/{id}", RESTAURANT_ID, MENU_ITEM_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MENU_ITEM_MATCHER.contentJson(MENU_ITEM_1_R_1_YSTRD));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(CURRENT_URL + "/{id}", RESTAURANT_ID, MENU_ITEM_ID + 1))
                .andExpect(status().isNoContent());
        MENU_ITEM_MATCHER.assertMatch(
                menuItemRepository.getByRestaurantAndDateInclusive(RESTAURANT_ID, LocalDate.now().minusDays(1), LocalDate.now()),
                List.of(MENU_ITEM_1_R_1_YSTRD, MENU_ITEM_3_R_1_YSTRD, MENU_ITEM_4_R_1_TODAY, MENU_ITEM_5_R_1_TODAY, MENU_ITEM_6_R_1_TODAY)
        );
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void update() throws Exception {
        MenuItem updated = getUpdatedMeal(menuItemRepository.findById(MENU_ITEM_ID).orElseThrow(
                () -> new EntityNotFoundException("Can`t find meal with  id = " + MENU_ITEM_ID)
        ));
        perform(MockMvcRequestBuilders.put(CURRENT_URL + "/{id}", RESTAURANT_ID, MENU_ITEM_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MENU_ITEM_MATCHER.assertMatch(menuItemRepository.findById(MENU_ITEM_ID).orElseThrow(
                () -> new EntityNotFoundException("Can`t find meal with  id = " + MENU_ITEM_ID)
        ), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void createOne() throws Exception {
        MenuItemTo newOne = MenuItemUtil.createFrom(getNewMeal());
        ResultActions action = perform(MockMvcRequestBuilders.post(CURRENT_URL, RESTAURANT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newOne)))
                .andExpect(status().isCreated())
                .andDo(print());
        MenuItemTo created = MENU_ITEM_TO_MATCHER.readFromJson(action);
        int id = created.getId();
        newOne.setId(id);
        MENU_ITEM_TO_MATCHER.assertMatch(created, newOne);
        MENU_ITEM_TO_MATCHER.assertMatch(MenuItemUtil.createFrom(menuItemRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find meal with  id = " + id)
        )), newOne);
    }
}