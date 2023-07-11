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
        RestaurantTo newTo = new RestaurantTo(origin.getId(), origin.getName(),
                origin.getAddress(), origin.getEmail(), null);
        newTo.setMenu(origin.getMenu() == null ? null : MenuItemUtil.getListTos(origin.getMenu()));
        return newTo;
    }

    public static List<RestaurantTo> getListTos(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantsUtil::createToFrom).collect(Collectors.toList());
    }

    public static List<RestaurantTo> getListTosWithMenu(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantsUtil::createToFromWithMenu).collect(Collectors.toList());
    }
}
