package topjava.restaurantvoting.to;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import topjava.restaurantvoting.model.BaseEntity;
import topjava.restaurantvoting.model.Meal;
import topjava.restaurantvoting.model.Vote;

import java.util.List;

public class RestaurantTo extends BaseEntity {

    @NotBlank
    private String name;


    @NotBlank
    private String address;

    @Email
    @NotBlank
    @Size(max = 128)
    private String email;

    private List<Meal> menu;

    @JsonIgnore
    private List<Vote> vote;
    private int voteCount;

    public RestaurantTo(Integer id, String name, String email, String address, Long voteCount) {
        super(id);
        this.name = name;
        this.email = email;
        this.address = address;
        this.voteCount = voteCount.intValue();
    }

    public RestaurantTo() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Meal> getMenu() {
        return menu;
    }

    public void setMenu(List<Meal> menu) {
        this.menu = menu;
    }

    public List<Vote> getVote() {
        return vote;
    }

    public void setVote(List<Vote> vote) {
        this.vote = vote;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
