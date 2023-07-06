package topjava.restaurantvoting.utils;

import topjava.restaurantvoting.model.Restaurant;
import topjava.restaurantvoting.to.RestaurantTo;

import java.util.List;
import java.util.stream.Collectors;

public class RestaurantsUtil {
    private RestaurantsUtil() {
    }

    public static Restaurant createFromTo(RestaurantTo to) {
        return new Restaurant(
                to.getId(),
                to.getName(),
                to.getAddress(),
                to.getEmail());
    }

    public static RestaurantTo createToFrom(Restaurant origin) {
        return new RestaurantTo(
                origin.getId(),
                origin.getName(),
                origin.getAddress(),
                origin.getEmail(),
                null);
    }

    public static RestaurantTo createToFromWithMenu(Restaurant origin) {
        return new RestaurantTo(
                origin.getId(),
                origin.getName(),
                origin.getAddress(),
                origin.getEmail(),
                origin.getMenu() == null ? null : MenuItemUtil.getListTos(origin.getMenu()),
                null);
    }

    public static List<RestaurantTo> getListTos(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantsUtil::createToFrom).collect(Collectors.toList());
    }

    public static List<RestaurantTo> getListTosWithMenu(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantsUtil::createToFromWithMenu).collect(Collectors.toList());
    }
}
