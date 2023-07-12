package com.github.sergjei.restaurant_voting;

import com.github.sergjei.restaurant_voting.model.Vote;
import com.github.sergjei.restaurant_voting.to.VoteTo;
import com.github.sergjei.restaurant_voting.utils.DateUtil;

import static com.github.sergjei.restaurant_voting.RestaurantTestData.RESTAURANT_1;
import static com.github.sergjei.restaurant_voting.RestaurantTestData.RESTAURANT_2;
import static com.github.sergjei.restaurant_voting.UserTestData.*;

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
