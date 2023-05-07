package topjava.restaurantvoting.to;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import topjava.restaurantvoting.model.User;
import topjava.restaurantvoting.model.Vote;

import java.util.List;

public class UserTo {
    @NotNull
    private Integer id;
    @NotBlank
    @Size(max = 128)
    private String firstName;

    @NotBlank
    @Size(max = 128)
    private String lastName;

    @NotBlank
    @Email
    @Size(max = 128)
    private String email;

    private List<Vote> votes;

    public UserTo(Integer id, String firstName,String lastName, String email, List<Vote> votes){
        this.id =id;
        this.firstName =firstName;
        this.lastName = lastName;
        this.email = email;
        this.votes = votes;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }


}
