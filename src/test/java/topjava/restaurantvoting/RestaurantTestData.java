package topjava.restaurantvoting;

import topjava.restaurantvoting.model.Restaurant;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "votes", "menu");
    public static final int RESTAURANT_ID = 1;
    public static final Restaurant RESTAURANT_1 = new Restaurant(RESTAURANT_ID, "Bistro", "French str", "bistro@gmail.com");
    public static final Restaurant RESTAURANT_2 = new Restaurant(RESTAURANT_ID + 1, "NeedForFood", "Belfast str", "fastfood@gmail.com");

    static {
        RESTAURANT_1.setMenu(MealTestData.R1_MENU_TODAY);
        RESTAURANT_2.setMenu(MealTestData.R2_MENU_TODAY);
    }

    public static Restaurant getNew() {
        return new Restaurant(null, "newName", "NewAddress", "newrest@gmail.com");
    }

    public static Restaurant getUpdated(Restaurant old) {
        Restaurant updated = new Restaurant();
        updated.setId(old.getId());
        updated.setName("updatedName");
        updated.setEmail("updated@gmail.com");
        updated.setAddress("updatedAdress");
        return updated;
    }
}
