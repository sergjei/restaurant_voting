package topjava.restaurantvoting.utils;

import topjava.restaurantvoting.model.Meal;
import topjava.restaurantvoting.to.MealTo;

import java.util.List;
import java.util.stream.Collectors;

public class MealsUtil {
    private MealsUtil() {
    }

    public static Meal updateFromToNoRest(MealTo to) {
        return new Meal(
                to.getId() == null ? null : to.getId(),
                to.getMenuDate(),
                to.getDescription(),
                to.getPrice()
        );
    }

    public static MealTo createFrom(Meal origin) {
        return new MealTo(origin.getId() == null ? null : origin.getId(),
                origin.getRestaurant().getId(),
                origin.getMenuDate(),
                origin.getDescription(),
                origin.getPrice());
    }

    public static List<MealTo> getListTos(List<Meal> meals) {
        return meals.stream().map(MealsUtil::createFrom).collect(Collectors.toList());
    }
}
