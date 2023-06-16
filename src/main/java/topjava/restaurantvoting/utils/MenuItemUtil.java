package topjava.restaurantvoting.utils;

import topjava.restaurantvoting.model.MenuItem;
import topjava.restaurantvoting.to.MenuItemTo;

import java.util.List;
import java.util.stream.Collectors;

public class MenuItemUtil {
    private MenuItemUtil() {
    }

    public static MenuItem updateFromToNoRest(MenuItemTo to) {
        return new MenuItem(
                to.getId() == null ? null : to.getId(),
                to.getMenuDate(),
                to.getName(),
                to.getPrice()
        );
    }

    public static MenuItemTo createFrom(MenuItem origin) {
        return new MenuItemTo(origin.getId() == null ? null : origin.getId(),
                origin.getRestaurant().getId(),
                origin.getMenuDate(),
                origin.getName(),
                origin.getPrice());
    }

    public static List<MenuItemTo> getListTos(List<MenuItem> menuItems) {
        return menuItems.stream().map(MenuItemUtil::createFrom).collect(Collectors.toList());
    }
}
