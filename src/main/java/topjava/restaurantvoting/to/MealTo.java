package topjava.restaurantvoting.to;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import topjava.restaurantvoting.model.BaseEntity;
import topjava.restaurantvoting.model.Meal;
import topjava.restaurantvoting.utils.ValidationUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class MealTo extends BaseEntity {
    @NotNull
    private Integer restaurant;

    @NotBlank
    private String description;

    @NotNull
    private LocalDate menuDate;

    @NotNull
    private Integer price;

    public MealTo(Integer id, Integer restaurant, LocalDate menuDate, String description, Integer price) {
        super(id);
        this.restaurant = restaurant;
        this.description = description;
        this.menuDate = menuDate;
        this.price = price;
    }

    public MealTo() {
    }

    public Integer getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Integer restaurant) {
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

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void updateFrom(Meal origin) {
        ValidationUtil.assureIdConsistentDef(this, origin.getId());
        this.setRestaurant(origin.getRestaurant().getId());
        this.setMenuDate(origin.getMenuDate());
        this.setDescription(origin.getDescription());
        this.setPrice(origin.getPrice());
    }

    public static MealTo createFrom(Meal origin) {
        return new MealTo(origin.getId(), origin.getRestaurant().getId(), origin.getMenuDate(), origin.getDescription(), origin.getPrice());
    }

    public static List<MealTo> getListTos(List<Meal> meals) {
        return meals.stream().map(MealTo::createFrom).collect(Collectors.toList());
    }
}
