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
                to.getId() == null ? null : to.getId(),
                to.getName(),
                to.getAddress(),
                to.getEmail());
    }

    public static RestaurantTo createToFrom(Restaurant origin) {
        return new RestaurantTo(
                origin.getId() == null ? null : origin.getId(),
                origin.getName(),
                origin.getAddress(),
                origin.getEmail(),
                origin.getMenu() == null ? null : MenuItemUtil.getListTos(origin.getMenu()),
                null);
    }

    public static List<RestaurantTo> getListTos(List<Restaurant> restaurants) {
        return restaurants.stream().map(RestaurantsUtil::createToFrom).collect(Collectors.toList());
    }
}
