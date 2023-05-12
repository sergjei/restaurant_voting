package topjava.restaurantvoting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import topjava.restaurantvoting.model.Restaurant;
import topjava.restaurantvoting.model.User;
import topjava.restaurantvoting.repository.RestaurantRepository;
import topjava.restaurantvoting.repository.UserRepository;
import topjava.restaurantvoting.repository.VoteRepository;
import topjava.restaurantvoting.to.RestaurantTo;
import topjava.restaurantvoting.utils.json.JsonUtil;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static topjava.restaurantvoting.RestaurantTestData.*;
import static topjava.restaurantvoting.UserTestData.*;
import static topjava.restaurantvoting.VoteTestData.*;

class AdminControllerTest extends AbstractControllerTest {
    public static final String CURRENT_URL = "/rest/admin";
    public static final String CURRENT_URL_USERS = "/rest/admin/users";
    @Autowired
    public UserRepository userRepository;
    @Autowired
    public RestaurantRepository restaurantRepository;
    @Autowired
    public VoteRepository voteRepository;

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(CURRENT_URL_USERS))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(List.of(USER, ADMIN, USER_2)));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(CURRENT_URL_USERS + "/{id}", USER_2_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(USER_2));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getUserByEmail() throws Exception {
        perform(MockMvcRequestBuilders.get(CURRENT_URL_USERS + "/by_email").param("email", USER_2.getEmail()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(USER_2));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void update() throws Exception {
        User updated = getUpdated(userRepository.findByEmailIgnoreCase(USER_2_EMAIL).orElseThrow());
        String json = JsonUtil.writeAdditionProp(updated, "password", "updatedPass");
        perform(MockMvcRequestBuilders.put(CURRENT_URL_USERS + "/{id}", USER_2_ID).contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userRepository.findByEmailIgnoreCase("updated@gmail.com").orElseThrow(), updated);
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void create() throws Exception {
        User newOne = getNew();
        String json = JsonUtil.writeAdditionProp(newOne, "password", "newPass");
        ResultActions action = perform(MockMvcRequestBuilders.post(CURRENT_URL_USERS)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated());
        User created = USER_MATCHER.readFromJson(action);
        newOne.setId(created.getId());
        USER_MATCHER.assertMatch(created, newOne);
        USER_MATCHER.assertMatch(userRepository.findById(created.getId()).orElseThrow(), newOne);
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(CURRENT_URL_USERS + "/{id}", USER_ID))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(userRepository.findAll(), List.of(ADMIN, USER_2));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getVotesCustom() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("startDate", LocalDate.now().minusDays(1).toString());
        parameters.add("endDate", LocalDate.now().minusDays(1).toString());
        parameters.add("users", List.of(1, 3).toString());
        parameters.add("restaurants", List.of(1).toString());
        perform(MockMvcRequestBuilders.get(CURRENT_URL + "/vote")
                .params(parameters))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(List.of(VOTE_1)));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getVotesByDateAndUser() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("startDate", LocalDate.now().minusDays(1).toString());
        parameters.add("endDate", LocalDate.now().minusDays(1).toString());
        parameters.add("users", List.of(1, 3).toString());
        perform(MockMvcRequestBuilders.get(CURRENT_URL + "/vote")
                .params(parameters))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VOTE_1, VOTE_3));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getVotesByDateAndRestaurant() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("startDate", LocalDate.now().minusDays(1).toString());
        parameters.add("endDate", LocalDate.now().minusDays(1).toString());
        parameters.add("restaurants", List.of(2).toString());
        perform(MockMvcRequestBuilders.get(CURRENT_URL + "/vote")
                .params(parameters))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VOTE_2, VOTE_3));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getVotesByDate() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("startDate", LocalDate.now().minusDays(1).toString());
        parameters.add("endDate", LocalDate.now().toString());
        perform(MockMvcRequestBuilders.get(CURRENT_URL + "/vote")
                .params(parameters))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VOTE_1, VOTE_2, VOTE_3));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getVotesDefault() throws Exception {
        perform(MockMvcRequestBuilders.get(CURRENT_URL + "/vote"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(VOTE_MATCHER.contentJson(VOTE_1, VOTE_2, VOTE_3));
    }

    @Test
    @WithUserDetails(ADMIN_EMAIL)
    void getVoteCount() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("startDate", LocalDate.now().minusDays(1).toString());
        parameters.add("endDate", LocalDate.now().minusDays(1).toString());
        ResultActions action = perform(MockMvcRequestBuilders.get(CURRENT_URL + "/vote_count")
                .params(parameters))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        List<RestaurantTo> restaurantTos = JsonUtil.readValues(action.andReturn().getResponse().getContentAsString(), RestaurantTo.class);
        assertEquals(2, restaurantTos.size());
        assertEquals(1, restaurantTos.get(0).getVoteCount());
        assertEquals(2, restaurantTos.get(1).getVoteCount());
        Restaurant firstRest = Restaurant.createFromTo(restaurantTos.get(0));
        Restaurant secondRest = Restaurant.createFromTo(restaurantTos.get(1));
        RESTAURANT_MATCHER.assertMatch(firstRest, RESTAURANT_1);
        RESTAURANT_MATCHER.assertMatch(secondRest, RESTAURANT_2);
    }
}