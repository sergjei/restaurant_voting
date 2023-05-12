package topjava.restaurantvoting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import topjava.restaurantvoting.model.Restaurant;
import topjava.restaurantvoting.repository.RestaurantRepository;
import topjava.restaurantvoting.utils.json.JsonUtil;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static topjava.restaurantvoting.RestaurantTestData.*;
import static topjava.restaurantvoting.UserTestData.ADMIN_EMAIL;

class RestaurantControllerTest extends AbstractControllerTest {
    public static final String CURRENT_URL = "/rest/admin/restaurant";
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(CURRENT_URL))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(List.of(RESTAURANT_1, RESTAURANT_2)));
    }

    @Test
    void getUnautherized() throws Exception {
        perform(MockMvcRequestBuilders.get(CURRENT_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(CURRENT_URL + "/{id}", RESTAURANT_ID + 1))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(RESTAURANT_MATCHER.contentJson(RESTAURANT_2));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(CURRENT_URL + "/{id}", RESTAURANT_ID))
                .andExpect(status().isNoContent());
        RESTAURANT_MATCHER.assertMatch(
                restaurantRepository.findAll(),
                RESTAURANT_2);
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void update() throws Exception {
        Restaurant updated = getUpdatedRestaurant(restaurantRepository.findById(RESTAURANT_ID + 1).orElseThrow());
        perform(MockMvcRequestBuilders.put(CURRENT_URL + "/{id}", RESTAURANT_ID + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.findById(RESTAURANT_ID + 1).orElseThrow(), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void create() throws Exception {
        Restaurant newOne = getNewRestaurant();
        ResultActions action = perform(MockMvcRequestBuilders.post(CURRENT_URL).contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newOne)))
                .andExpect(status().isCreated())
                .andDo(print());
        Restaurant created = RESTAURANT_MATCHER.readFromJson(action);
        newOne.setId(created.getId());
        RESTAURANT_MATCHER.assertMatch(created, newOne);
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.findById(created.getId()).orElseThrow(), newOne);
    }
}