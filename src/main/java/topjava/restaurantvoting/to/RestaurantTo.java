package topjava.restaurantvoting.to;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import topjava.restaurantvoting.model.BaseEntity;

import java.util.List;

public class RestaurantTo extends BaseEntity {
    @NotBlank
    private String name;
    @NotBlank
    private String address;

    public List<MenuItemTo> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuItemTo> menu) {
        this.menu = menu;
    }

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private List<MenuItemTo> menu;

    @Email
    @NotBlank
    @Size(max = 128)
    private String email;
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private Integer voteCount;

    public RestaurantTo(Integer id, String name, String address, String email, Long voteCount) {
        super(id);
        this.name = name;
        this.address = address;
        this.email = email;
        this.menu = null;
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
