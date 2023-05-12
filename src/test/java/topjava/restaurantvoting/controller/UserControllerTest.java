package topjava.restaurantvoting.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import topjava.restaurantvoting.RestaurantTestData;
import topjava.restaurantvoting.model.Restaurant;
import topjava.restaurantvoting.model.User;
import topjava.restaurantvoting.model.Vote;
import topjava.restaurantvoting.repository.UserRepository;
import topjava.restaurantvoting.utils.json.JsonUtil;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static topjava.restaurantvoting.MealTestData.*;
import static topjava.restaurantvoting.RestaurantTestData.*;
import static topjava.restaurantvoting.UserTestData.*;
import static topjava.restaurantvoting.VoteTestData.*;

class UserControllerTest extends AbstractControllerTest {
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
    @WithUserDetails(value = USER_EMAIL)
    void update() throws Exception {
        User updated = getUpdated(userRepository.findByEmailIgnoreCase(USER_EMAIL).orElseThrow(
                () -> new EntityNotFoundException("Can`t find user with  email = " + USER_EMAIL)
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
                .andExpect(RESTAURANT_MATCHER.contentJson(List.of(RestaurantTestData.RESTAURANT_1, RESTAURANT_2)));
        List<Restaurant> restaurants = JsonUtil.readValues(action.andReturn().getResponse().getContentAsString(), Restaurant.class);
        MEAL_MATCHER.assertMatch(restaurants.get(0).getMenu(), R1_MENU_TODAY);
        MEAL_MATCHER.assertMatch(restaurants.get(1).getMenu(), R2_MENU_TODAY);
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getVotes() throws Exception {
        setVotes();
        setVotesRest();
        ResultActions action = perform(MockMvcRequestBuilders.get(CURRENT_URL + "/vote"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        List<Vote> actual = JsonUtil.readValues(action.andReturn().getResponse().getContentAsString(), Vote.class);
        assertEquals(1, actual.size());
        VOTE_MATCHER.assertMatch(actual, VOTE_2);
        RESTAURANT_MATCHER.assertMatch(actual.get(0).getRestaurant(), RESTAURANT_2);
        USER_MATCHER.assertMatch(actual.get(0).getUser(), ADMIN);
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void vote() throws Exception {
        setVotes();
        ResultActions action = perform(MockMvcRequestBuilders.post(CURRENT_URL + "/vote")
                .param("restId", "1"))
                .andDo(print())
                .andExpect(status().isCreated());
        Vote created = VOTE_MATCHER.readFromJson(action);
        Vote newOne = getNewVote();
        newOne.setId(created.getId());
        VOTE_MATCHER.assertMatch(created, newOne);
    }

    @Test
    @WithUserDetails(value = USER_EMAIL)
    void changeVote() throws Exception {
        setVotes();
        ResultActions actionCreate = perform(MockMvcRequestBuilders.post(CURRENT_URL + "/vote")
                .param("restId", "1"))
                .andDo(print())
                .andExpect(status().isCreated());
        Vote created = VOTE_MATCHER.readFromJson(actionCreate);
        ResultActions actionUpdate = perform(MockMvcRequestBuilders.put(CURRENT_URL + "/vote/{id}", created.getId())
                .param("restId", "2"))
                .andDo(print());
        Vote updated = VOTE_MATCHER.readFromJson(actionUpdate);
        if (LocalTime.now().isBefore(LocalTime.of(11, 0, 0))) {
            created.setRestaurant(RESTAURANT_2);
        }
        VOTE_MATCHER.assertMatch(updated, created);
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
}