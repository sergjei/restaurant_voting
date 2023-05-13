package topjava.restaurantvoting.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import topjava.restaurantvoting.to.MealTo;
import topjava.restaurantvoting.utils.ValidationUtil;
import topjava.restaurantvoting.utils.json.MealCustomDeserializer;
import topjava.restaurantvoting.utils.json.MealCustomSerializer;

import java.time.LocalDate;

@Entity
@JsonSerialize(using = MealCustomSerializer.class)
@JsonDeserialize(using = MealCustomDeserializer.class)
@Table(name = "meal", indexes = {
        @Index(name = "restaurant_uniq_meal_date", columnList = "menu_date,rest_id,description", unique = true),
        @Index(name = "restaurant_menu_date", columnList = "menu_date, rest_id")
})
public class Meal extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rest_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
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

    public Meal(Integer id, Restaurant restaurant, LocalDate menuDate, String description, int price) {
        super(id);
        this.restaurant = restaurant;
        this.description = description;
        this.menuDate = menuDate;
        this.price = price;
    }

    public Meal(Integer id, LocalDate menuDate, String description, int price) {
        super(id);
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

    public void setDescription(String description) {
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

    public void updateFromToNoRest(MealTo to) {
        if (!this.isNew()) {
            ValidationUtil.assureIdConsistentDef(this, to.getId());
        } else {
            this.id = to.getId() == null ? null : to.getId();
        }
        this.price = to.getPrice();
        this.description = to.getDescription();
        this.menuDate = to.getMenuDate();
    }
}
