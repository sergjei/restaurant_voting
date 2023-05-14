package topjava.restaurantvoting.to;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import topjava.restaurantvoting.model.BaseEntity;

public class RestaurantTo extends BaseEntity {
    @NotBlank
    private String name;
    @NotBlank
    private String address;
    @Email
    @NotBlank
    @Size(max = 128)
    private String email;
    private Integer voteCount;

    public RestaurantTo(Integer id, String name, String email, String address, Long voteCount) {
        super(id);
        this.name = name;
        this.email = email;
        this.address = address;
        this.voteCount = voteCount == null ? null : voteCount.intValue();
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

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }
}
