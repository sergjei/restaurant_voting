package com.github.sergjei.restaurant_voting;

import com.github.sergjei.restaurant_voting.model.Role;
import com.github.sergjei.restaurant_voting.model.User;
import com.github.sergjei.restaurant_voting.utils.json.JsonUtil;

import java.util.Collections;
import java.util.List;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "votes", "password");
    public static final String USER_EMAIL = "user@gmail.com";
    public static final String ADMIN_EMAIL = "admin@gmail.com";
    public static final String USER_2_EMAIL = "user2@gmail.com";
    public static final int USER_ID = 1;
    public static final int ADMIN_ID = 2;
    public static final int USER_2_ID = 3;
    public static final User USER = new User(USER_ID, "Sergey", "Plot", "user@gmail.com", "{noop}password", Role.USER);
    public static final User ADMIN = new User(ADMIN_ID, "Admin", "Adminsky", "admin@gmail.com", "{noop}admin", Role.USER, Role.ADMIN);
    public static final User USER_2 = new User(USER_2_ID, "User", "Guestski", "user2@gmail.com", "{noop}guest", Role.USER);

    public static User getNew() {
        return new User(null, "newName", "newLastName", "new@gmail.com", "newPass", Role.USER);
    }

    public static void setVotes() {
        USER.setVotes(List.of(VoteTestData.VOTE_1));
        USER.setVotes(List.of(VoteTestData.VOTE_4));
        ADMIN.setVotes(List.of(VoteTestData.VOTE_2));
        USER_2.setVotes(List.of(VoteTestData.VOTE_3));
    }

    public static User getUpdatedAdmin(User old) {
        User updated = new User();
        updated.setId(old.getId());
        updated.setPassword("updatedPass");
        updated.setEmail("updated@gmail.com");
        updated.setFirstName("updatedName");
        updated.setLastName("updatedLastName");
        updated.setRoles(Collections.singletonList(Role.ADMIN));
        return updated;
    }

    public static User getUpdated(User old) {
        User updated = new User();
        updated.setId(old.getId());
        updated.setPassword("updatedPass");
        updated.setEmail("updated@gmail.com");
        updated.setFirstName("updatedName");
        updated.setLastName("updatedLastName");
        updated.setRoles(Collections.singletonList(Role.USER));
        return updated;
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProp(user, "password", passw);
    }
}
