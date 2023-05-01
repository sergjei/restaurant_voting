package topjava.restaurantvoting.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vote")
public class Vote extends BaseEntity {
    @Column(name = "vote_datetime")
    private LocalDateTime voteDateTime;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "rest_id", nullable = false)
    private Restaurant restaurant;

    public Vote() {
    }

    public Vote(Integer id, LocalDateTime voteDateTime, User user, Restaurant restaurant) {
        super(id);
        this.voteDateTime = voteDateTime;
        this.user = user;
        this.restaurant = restaurant;
    }

    public LocalDateTime getVoteDateTime() {
        return voteDateTime;
    }

    public void setVoteDateTime(LocalDateTime voteDateTime) {
        this.voteDateTime = voteDateTime;
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
