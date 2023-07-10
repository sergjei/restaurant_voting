package topjava.restaurantvoting.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import topjava.restaurantvoting.RestaurantTestData;
import topjava.restaurantvoting.model.User;
import topjava.restaurantvoting.repository.UserRepository;
import topjava.restaurantvoting.repository.VoteRepository;
import topjava.restaurantvoting.to.RestaurantTo;
import topjava.restaurantvoting.to.VoteTo;
import topjava.restaurantvoting.utils.DateUtil;
import topjava.restaurantvoting.utils.MenuItemUtil;
import topjava.restaurantvoting.utils.RestaurantsUtil;
import topjava.restaurantvoting.utils.VotesUtil;
import topjava.restaurantvoting.utils.json.JsonUtil;

import java.time.Clock;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static topjava.restaurantvoting.MenuItemTestData.*;
import static topjava.restaurantvoting.RestaurantTestData.*;
import static topjava.restaurantvoting.UserTestData.*;
import static topjava.restaurantvoting.VoteTestData.*;

class ProfileControllerTest extends AbstractControllerTest {
    public static final String CURRENT_URL = "/rest/profile";
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VoteRepository voteRepository;

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
        ResultActions actionUpdate = perform(MockMvcRequestBuilders.put(CURRENT_URL + "/votes/{id}", VOTE_ID + 3)
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
        perform(MockMvcRequestBuilders.put(CURRENT_URL + "/votes/{id}", VOTE_ID + 3)
                .param("restaurantId", "2"))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(result -> assertEquals("422 UNPROCESSABLE_ENTITY \"Vote can be changed only before 11 AM\"", result.getResolvedException().getMessage()));
        DateUtil.initDefaultClock();
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