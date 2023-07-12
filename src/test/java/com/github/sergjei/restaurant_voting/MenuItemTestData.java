package com.github.sergjei.restaurant_voting;

import com.github.sergjei.restaurant_voting.model.MenuItem;
import com.github.sergjei.restaurant_voting.to.MenuItemTo;
import com.github.sergjei.restaurant_voting.utils.DateUtil;

import java.time.LocalDate;
import java.util.List;

import static com.github.sergjei.restaurant_voting.RestaurantTestData.RESTAURANT_1;

public class MenuItemTestData {
    public static final MatcherFactory.Matcher<MenuItem> MENU_ITEM_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuItem.class, "restaurant");
    public static final MatcherFactory.Matcher<MenuItemTo> MENU_ITEM_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MenuItemTo.class, "restaurant");
    public static final int MENU_ITEM_ID = 1;
    public static final MenuItem MENU_ITEM_1_R_1_YSTRD = new MenuItem(MENU_ITEM_ID, DateUtil.TODAY.minusDays(1), "Hot-dog", 700);
    public static final MenuItem MENU_ITEM_2_R_1_YSTRD = new MenuItem(MENU_ITEM_ID + 1, DateUtil.TODAY.minusDays(1), "pizza", 1200);
    public static final MenuItem MENU_ITEM_3_R_1_YSTRD = new MenuItem(MENU_ITEM_ID + 2, DateUtil.TODAY.minusDays(1), "pasta", 1000);
    public static final MenuItem MENU_ITEM_1_R_2_YSTRD = new MenuItem(MENU_ITEM_ID + 3, DateUtil.TODAY.minusDays(1), "rolls", 2000);
    public static final MenuItem MENU_ITEM_2_R_2_YSTRD = new MenuItem(MENU_ITEM_ID + 4, DateUtil.TODAY.minusDays(1), "burger", 1200);
    public static final MenuItem MENU_ITEM_3_R_2_YSTRD = new MenuItem(MENU_ITEM_ID + 5, DateUtil.TODAY.minusDays(1), "buritto", 1200);
    public static final MenuItem MENU_ITEM_4_R_1_TODAY = new MenuItem(MENU_ITEM_ID + 6, DateUtil.TODAY, "bulmeni", 700);
    public static final MenuItem MENU_ITEM_5_R_1_TODAY = new MenuItem(MENU_ITEM_ID + 7, DateUtil.TODAY, "ragu", 1200);
    public static final MenuItem MENU_ITEM_6_R_1_TODAY = new MenuItem(MENU_ITEM_ID + 8, DateUtil.TODAY, "cotleties", 1000);
    public static final MenuItem MENU_ITEM_4_R_2_TODAY = new MenuItem(MENU_ITEM_ID + 9, DateUtil.TODAY, "midiies", 2200);
    public static final MenuItem MENU_ITEM_5_R_2_TODAY = new MenuItem(MENU_ITEM_ID + 10, DateUtil.TODAY, "shrimps", 1700);
    public static final MenuItem MENU_ITEM_6_R_2_TODAY = new MenuItem(MENU_ITEM_ID + 11, DateUtil.TODAY, "calmar", 1200);
    public static final MenuItem MENU_ITEM_7_TMRW = new MenuItem(MENU_ITEM_ID + 12, DateUtil.TODAY.plusDays(1), "porridge with pork", 1700);
    public static final MenuItem MENU_ITEM_8_TMRW = new MenuItem(MENU_ITEM_ID + 13, DateUtil.TODAY.plusDays(1), "pasta with cheese", 900);
    public static final MenuItem MENU_ITEM_9_TMRW = new MenuItem(MENU_ITEM_ID + 14, DateUtil.TODAY.plusDays(1), "fried vegatables", 1200);
    public static final List<MenuItem> R1_MENU_YSTRD = List.of(MENU_ITEM_1_R_1_YSTRD, MENU_ITEM_2_R_1_YSTRD, MENU_ITEM_3_R_1_YSTRD);
    public static final List<MenuItem> R2_MENU_YSTRD = List.of(MENU_ITEM_1_R_2_YSTRD, MENU_ITEM_2_R_2_YSTRD, MENU_ITEM_3_R_2_YSTRD);
    public static final List<MenuItem> R1_MENU_TODAY = List.of(MENU_ITEM_4_R_1_TODAY, MENU_ITEM_5_R_1_TODAY, MENU_ITEM_6_R_1_TODAY);
    public static final List<MenuItem> R2_MENU_TODAY = List.of(MENU_ITEM_4_R_2_TODAY, MENU_ITEM_5_R_2_TODAY, MENU_ITEM_6_R_2_TODAY);
    public static final List<MenuItem> MENU_TODAY = List.of(MENU_ITEM_4_R_1_TODAY, MENU_ITEM_5_R_1_TODAY, MENU_ITEM_6_R_1_TODAY, MENU_ITEM_4_R_2_TODAY, MENU_ITEM_5_R_2_TODAY, MENU_ITEM_6_R_2_TODAY);
    public static final List<MenuItem> R1_MENU_ALL = List.of(MENU_ITEM_1_R_1_YSTRD, MENU_ITEM_2_R_1_YSTRD, MENU_ITEM_3_R_1_YSTRD, MENU_ITEM_4_R_1_TODAY, MENU_ITEM_5_R_1_TODAY, MENU_ITEM_6_R_1_TODAY);
    public static final List<MenuItem> MENU_TMRW = List.of(MENU_ITEM_7_TMRW, MENU_ITEM_8_TMRW, MENU_ITEM_9_TMRW);

    public static MenuItem getNewMenuItem() {
        return new MenuItem(null, RESTAURANT_1, DateUtil.getToday(), "newFood", 666);
    }

    public static List<MenuItem> getTomorrowMenuItems() {
        return List.of(new MenuItem(null, RESTAURANT_1, DateUtil.getToday().plusDays(1), "porridge with pork", 1700),
                new MenuItem(null, RESTAURANT_1, DateUtil.getToday().plusDays(1), "pasta with cheese", 900),
                new MenuItem(null, RESTAURANT_1, DateUtil.getToday().plusDays(1), "fried vegatables", 1200));
    }

    public static MenuItem getUpdatedMenuItem(MenuItem old) {
        MenuItem updated = new MenuItem();
        updated.setId(old.getId());
        updated.setName("updatedName");
        updated.setMenuDate(LocalDate.of(2000, 1, 2));
        updated.setPrice(555);
        updated.setRestaurant(old.getRestaurant());
        return updated;
    }
}
