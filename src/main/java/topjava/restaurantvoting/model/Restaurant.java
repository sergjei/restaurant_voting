package topjava.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@Table(name = "restaurant", indexes = @Index(name = "rest_name_address", columnList = "name,address", unique = true))
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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OrderBy("menuDate DESC")
    private List<MenuItem> menu;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "restaurant")
    @OrderBy("voteDate DESC")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Vote> votes;

    public Restaurant(Integer id, String name, String address, String email, List<MenuItem> menu) {
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

    public List<MenuItem> getMenu() {
        return menu;
    }

    public void setMenu(List<MenuItem> menu) {
        this.menu = menu;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }
}
