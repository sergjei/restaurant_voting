package topjava.restaurantvoting.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "meal", indexes = {
        @Index(name = "restaurant_uniq_meal_date", columnList = "menu_date,rest_id,description", unique = true),
        @Index(name = "restaurant_menu_date", columnList = "menu_date, rest_id")
})
public class Meal extends BaseEntity {
    @JsonIgnore
    @ManyToOne//(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    @Column(name = "description")
    @NotBlank
    private String description;

    @Column(name = "menu_date")
    @NotNull
    private LocalDate menuDate;

    @Column(name = "price")
    @NotNull
    private int price;

    public Meal(Integer id, Restaurant restaurant, String description, LocalDate menuDate, int price) {
        super(id);
        this.restaurant = restaurant;
        this.description = description;
        this.menuDate = menuDate;
        this.price = price;
    }

    public Meal() {
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String description) {
        this.description = description;
    }

    public LocalDate getMenuDate() {
        return menuDate;
    }

    public void setMenuDate(LocalDate menuDate) {
        this.menuDate = menuDate;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
