package topjava.restaurantvoting;

import topjava.restaurantvoting.model.Restaurant;

import java.util.List;

public class RestaurantTestData {
    public static final MatcherFactory.Matcher<Restaurant> RESTAURANT_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Restaurant.class, "votes", "menu");
    public static final int RESTAURANT_ID = 1;
    public static final Restaurant RESTAURANT_1 = new Restaurant(RESTAURANT_ID, "Bistro", "French str", "bistro@gmail.com");
    public static final Restaurant RESTAURANT_2 = new Restaurant(RESTAURANT_ID + 1, "NeedForFood", "Belfast str", "fastfood@gmail.com");

    public static Restaurant getNewRestaurant() {
        return new Restaurant(null, "newName", "NewAddress", "newrest@gmail.com");
    }

    public static void setTodayMenu() {
        RESTAURANT_1.setMenu(MealTestData.R1_MENU_TODAY);
        RESTAURANT_2.setMenu(MealTestData.R2_MENU_TODAY);
    }

    public static void setVotesRest() {
        RESTAURANT_1.setVotes(List.of(VoteTestData.VOTE_1));
        RESTAURANT_2.setVotes(List.of(VoteTestData.VOTE_2, VoteTestData.VOTE_3));
    }

    public static Restaurant getUpdatedRestaurant(Restaurant old) {
        Restaurant updated = new Restaurant();
        updated.setId(old.getId());
        updated.setName("updatedName");
        updated.setEmail("updated@gmail.com");
        updated.setAddress("updatedAdress");
        return updated;
    }
}
