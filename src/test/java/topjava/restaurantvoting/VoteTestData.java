package topjava.restaurantvoting;

import topjava.restaurantvoting.model.Vote;
import topjava.restaurantvoting.to.VoteTo;
import topjava.restaurantvoting.utils.DateUtil;

import static topjava.restaurantvoting.RestaurantTestData.RESTAURANT_1;
import static topjava.restaurantvoting.RestaurantTestData.RESTAURANT_2;
import static topjava.restaurantvoting.UserTestData.*;

public class VoteTestData {
    public static final MatcherFactory.Matcher<Vote> VOTE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Vote.class, "user", "restaurant");
    public static final MatcherFactory.Matcher<VoteTo> VOTE_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(VoteTo.class);
    public static final int VOTE_ID = 1;
    public static final Vote VOTE_1 = new Vote(VOTE_ID, DateUtil.TODAY.minusDays(1), USER, RESTAURANT_1);
    public static final Vote VOTE_2 = new Vote(VOTE_ID + 1, DateUtil.TODAY.minusDays(1), ADMIN, RESTAURANT_2);
    public static final Vote VOTE_3 = new Vote(VOTE_ID + 2, DateUtil.TODAY.minusDays(1), USER_2, RESTAURANT_2);
    public static final Vote VOTE_4 = new Vote(VOTE_ID + 3, DateUtil.TODAY, USER, RESTAURANT_1);

    public static Vote getNewVote() {
        return new Vote(null, DateUtil.getToday(), USER_2, RESTAURANT_1);
    }
}
