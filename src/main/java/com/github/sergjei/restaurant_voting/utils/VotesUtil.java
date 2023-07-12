package com.github.sergjei.restaurant_voting.utils;

import com.github.sergjei.restaurant_voting.model.Vote;
import com.github.sergjei.restaurant_voting.to.VoteTo;

import java.util.List;
import java.util.stream.Collectors;

public class VotesUtil {
    private VotesUtil() {
    }

    public static VoteTo createToFrom(Vote origin) {
        return new VoteTo(
                origin.getId(),
                origin.getVoteDate(),
                origin.getUser().getId(),
                origin.getRestaurant().getId());
    }

    public static List<VoteTo> getListTos(List<Vote> votes) {
        return votes.stream().map(VotesUtil::createToFrom).collect(Collectors.toList());
    }
}
