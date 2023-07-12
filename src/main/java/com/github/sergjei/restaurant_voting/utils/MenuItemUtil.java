package com.github.sergjei.restaurant_voting.utils;

import com.github.sergjei.restaurant_voting.model.MenuItem;
import com.github.sergjei.restaurant_voting.to.MenuItemTo;

import java.util.List;
import java.util.stream.Collectors;

public class MenuItemUtil {
    private MenuItemUtil() {
    }

    public static MenuItem updateFromToNoRest(MenuItemTo to) {
        return new MenuItem(
                to.getId(),
                to.getMenuDate(),
                to.getName(),
                to.getPrice()
        );
    }

    public static MenuItemTo createToFrom(MenuItem origin) {
        return new MenuItemTo(origin.getId(),
                origin.getRestaurant() == null ? null : origin.getRestaurant().getId(),
                origin.getMenuDate(),
                origin.getName(),
                origin.getPrice());
    }

    public static List<MenuItemTo> getListTos(List<MenuItem> menuItems) {
        return menuItems.stream().map(MenuItemUtil::createToFrom).collect(Collectors.toList());
    }
}
