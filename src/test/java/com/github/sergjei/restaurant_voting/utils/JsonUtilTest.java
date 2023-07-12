package com.github.sergjei.restaurant_voting.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import com.github.sergjei.restaurant_voting.config.WebSecurityConfig;
import com.github.sergjei.restaurant_voting.model.MenuItem;
import com.github.sergjei.restaurant_voting.model.Restaurant;
import com.github.sergjei.restaurant_voting.model.User;
import com.github.sergjei.restaurant_voting.model.Vote;
import com.github.sergjei.restaurant_voting.utils.json.JsonUtil;

import java.util.List;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static com.github.sergjei.restaurant_voting.MenuItemTestData.*;
import static com.github.sergjei.restaurant_voting.RestaurantTestData.RESTAURANT_1;
import static com.github.sergjei.restaurant_voting.RestaurantTestData.RESTAURANT_MATCHER;
import static com.github.sergjei.restaurant_voting.UserTestData.*;
import static com.github.sergjei.restaurant_voting.VoteTestData.VOTE_1;
import static com.github.sergjei.restaurant_voting.VoteTestData.VOTE_MATCHER;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@ActiveProfiles("test")
class JsonUtilTest {
    @Test
    void readWriteValueMenuItem() {
        String json = JsonUtil.writeValue(MENU_ITEM_1_R_1_YSTRD);
        MenuItem menuItem = JsonUtil.readValue(json, MenuItem.class);
        MENU_ITEM_MATCHER.assertMatch(menuItem, MENU_ITEM_1_R_1_YSTRD);
    }

    @Test
    void readWriteValueRestaurant() {
        String json = JsonUtil.writeValue(RESTAURANT_1);
        Restaurant restaurant = JsonUtil.readValue(json, Restaurant.class);
        RESTAURANT_MATCHER.assertMatch(restaurant, RESTAURANT_1);
    }

    @Test
    void readWriteValueVote() {
        String json = JsonUtil.writeValue(VOTE_1);
        Vote vote = JsonUtil.readValue(json, Vote.class);
        VOTE_MATCHER.assertMatch(vote, VOTE_1);
    }

    @Test
    void readWriteValueUser() {
        String json = JsonUtil.writeValue(USER);
        User user = JsonUtil.readValue(json, User.class);
        USER_MATCHER.assertMatch(user, USER);
    }

    @Test
    void readWriteValues() {
        String json = JsonUtil.writeValue(R1_MENU_TODAY);
        List<MenuItem> menu = JsonUtil.readValues(json, MenuItem.class);
        MENU_ITEM_MATCHER.assertMatch(menu, R1_MENU_TODAY);
    }

    @Test
    void writeOnlyAccess() {
        String json = JsonUtil.writeValue(USER);
        System.out.println(json);
        assertThat(json, not(containsString("password")));
        String jsonWithPass = jsonWithPassword(USER, "newPass");
        System.out.println(jsonWithPass);
        User user = JsonUtil.readValue(jsonWithPass, User.class);
        assertEquals(WebSecurityConfig.ENCODER.matches("newPass", user.getPassword()), true);
    }
}