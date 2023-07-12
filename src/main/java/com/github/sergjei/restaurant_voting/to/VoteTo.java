package com.github.sergjei.restaurant_voting.to;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;
import com.github.sergjei.restaurant_voting.model.BaseEntity;
import com.github.sergjei.restaurant_voting.model.Vote;
import com.github.sergjei.restaurant_voting.utils.ValidationUtil;

import java.time.LocalDate;

public class VoteTo extends BaseEntity {

    @DateTimeFormat
    @NotNull
    @PastOrPresent
    private LocalDate voteDate;

    @NotNull
    private Integer userId;

    @NotNull
    private Integer restaurantId;

    public VoteTo() {
    }

    public VoteTo(Integer id, LocalDate voteDate, Integer userId, Integer restaurantId) {
        super(id);
        this.voteDate = voteDate;
        this.userId = userId;
        this.restaurantId = restaurantId;
    }

    public LocalDate getVoteDate() {
        return voteDate;
    }

    public void setVoteDate(LocalDate voteDate) {
        this.voteDate = voteDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void updateFrom(Vote origin) {
        ValidationUtil.assureIdConsistentDef(this, origin.getId());
        this.setVoteDate(origin.getVoteDate());
        this.setUserId(origin.getUser().getId());
        this.setRestaurantId(origin.getRestaurant().getId());
    }
}
