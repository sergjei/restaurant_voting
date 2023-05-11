package topjava.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;

@Entity
@Table(name = "restaurant", indexes = @Index(name = "rest_email_address", columnList = "email,address", unique = true))
public class Restaurant extends BaseEntity {
    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "address")
    @NotBlank
    private String address;
    @Column(name = "email")
    @Email
    @NotBlank
    @Size(max = 128)
    private String email;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "restaurant")
    @OrderBy("menuDate DESC")
    private List<Meal> menu;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy("voteDate DESC")
    private List<Vote> vote;

    public Restaurant(Integer id, String name, String address, String email, List<Meal> menu) {
        super(id);
        this.name = name;
        this.address = address;
        this.email = email;
        this.menu = menu;
    }
    public Restaurant(Integer id, String name, String address, String email) {
        super(id);
        this.name = name;
        this.address = address;
        this.email = email;
    }
    public Restaurant() {
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
}
