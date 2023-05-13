package topjava.restaurantvoting.to;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import org.springframework.format.annotation.DateTimeFormat;
import topjava.restaurantvoting.model.BaseEntity;
import topjava.restaurantvoting.model.Vote;
import topjava.restaurantvoting.utils.ValidationUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    public static VoteTo createFrom(Vote origin) {
        return new VoteTo(origin.getId(), origin.getVoteDate(), origin.getUser().getId(), origin.getRestaurant().getId());
    }

    public static List<VoteTo> getListTos(List<Vote> votes) {
        return votes.stream().map((v) -> VoteTo.createFrom(v)).collect(Collectors.toList());
    }
}
