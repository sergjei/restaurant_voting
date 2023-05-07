package topjava.restaurantvoting.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import topjava.restaurantvoting.VoteCustomDeserializer;
import topjava.restaurantvoting.VoteCustomSerializer;

import java.time.LocalDate;

@JsonSerialize(using = VoteCustomSerializer.class)
@JsonDeserialize(using = VoteCustomDeserializer.class)
@Entity
@Table(name = "vote", indexes = @Index(name = "user_vote_per_day", columnList = "user_id,vote_date", unique = true))
public class Vote extends BaseEntity {
    @Column(name = "vote_date")
    private LocalDate voteDate;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "rest_id", nullable = false)
    private Restaurant restaurant;

    public Vote() {
    }
    public Vote(Integer id, LocalDate voteDate, User user, Restaurant restaurant) {
        super(id);
        this.voteDate = voteDate;
        this.user = user;
        this.restaurant = restaurant;
    }
    public Vote(Integer id,  User user, Restaurant restaurant) {
        super(id);
        this.voteDate = LocalDate.now();
        this.user = user;
        this.restaurant = restaurant;
    }

    public LocalDate getVoteDate() {
        return voteDate;
    }

    public void setVoteDate(LocalDate voteDate) {
        this.voteDate = voteDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
