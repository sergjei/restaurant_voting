package topjava.restaurantvoting;

import topjava.restaurantvoting.model.Vote;

import java.time.LocalDate;

import static topjava.restaurantvoting.RestaurantTestData.RESTAURANT_1;
import static topjava.restaurantvoting.RestaurantTestData.RESTAURANT_2;
import static topjava.restaurantvoting.UserTestData.*;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingEqualComparator(Vote.class);
    public static final int VOTE_ID = 1;
    public static final Vote VOTE_1 = new Vote(VOTE_ID, LocalDate.now().minusDays(1), USER, RESTAURANT_1);
    public static final Vote VOTE_2 = new Vote(VOTE_ID + 1, LocalDate.now().minusDays(1), ADMIN, RESTAURANT_2);
    public static final Vote VOTE_3 = new Vote(VOTE_ID + 2, LocalDate.now().minusDays(1), USER_2, RESTAURANT_2);
}
