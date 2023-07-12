package com.github.sergjei.restaurant_voting.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;
import com.github.sergjei.restaurant_voting.utils.DateUtil;

import java.time.LocalDate;

@Entity
@Table(name = "vote", indexes = {
        @Index(name = "user_vote_per_day", columnList = "user_id,vote_date", unique = true),
        @Index(name = "vote_per_restaurant_per_day", columnList = "restaurant_id,vote_date")
})
public class Vote extends com.github.sergjei.restaurant_voting.model.BaseEntity {
    @Column(name = "vote_date")
    @DateTimeFormat
    @NotNull
    @PastOrPresent
    private LocalDate voteDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull
    private com.github.sergjei.restaurant_voting.model.User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private com.github.sergjei.restaurant_voting.model.Restaurant restaurant;

    public Vote() {
    }

    public Vote(Integer id, LocalDate voteDate, com.github.sergjei.restaurant_voting.model.User user, com.github.sergjei.restaurant_voting.model.Restaurant restaurant) {
        super(id);
        this.voteDate = voteDate;
        this.user = user;
        this.restaurant = restaurant;
    }

    public Vote(Integer id, com.github.sergjei.restaurant_voting.model.User user, com.github.sergjei.restaurant_voting.model.Restaurant restaurant) {
        super(id);
        this.voteDate = DateUtil.getToday();
        this.user = user;
        this.restaurant = restaurant;
    }

    public LocalDate getVoteDate() {
        return voteDate;
    }

    public void setVoteDate(LocalDate voteDate) {
        this.voteDate = voteDate;
    }

    public com.github.sergjei.restaurant_voting.model.User getUser() {
        return user;
    }

    public void setUser(com.github.sergjei.restaurant_voting.model.User user) {
        this.user = user;
    }

    public com.github.sergjei.restaurant_voting.model.Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(com.github.sergjei.restaurant_voting.model.Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
