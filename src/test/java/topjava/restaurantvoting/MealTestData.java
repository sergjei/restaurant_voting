package topjava.restaurantvoting;

import topjava.restaurantvoting.model.Meal;
import topjava.restaurantvoting.to.MealTo;

import java.time.LocalDate;
import java.util.List;

import static topjava.restaurantvoting.RestaurantTestData.RESTAURANT_1;

public class MealTestData {
    public static final MatcherFactory.Matcher<Meal> MEAL_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Meal.class, "restaurant");
    public static final MatcherFactory.Matcher<MealTo> MEAL_TO_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(MealTo.class);
    public static final int MEAL_ID = 1;
    public static final Meal MEAL_1_R1_YSTRD = new Meal(MEAL_ID, LocalDate.now().minusDays(1), "Hot-dog", 700);
    public static final Meal MEAL_2_R1_YSTRD = new Meal(MEAL_ID + 1, LocalDate.now().minusDays(1), "pizza", 1200);
    public static final Meal MEAL_3_R1_YSTRD = new Meal(MEAL_ID + 2, LocalDate.now().minusDays(1), "pasta", 1000);
    public static final Meal MEAL_1_R2_YSTRD = new Meal(MEAL_ID + 3, LocalDate.now().minusDays(1), "rolls", 2000);
    public static final Meal MEAL_2_R2_YSTRD = new Meal(MEAL_ID + 4, LocalDate.now().minusDays(1), "burger", 1200);
    public static final Meal MEAL_3_R2_YSTRD = new Meal(MEAL_ID + 5, LocalDate.now().minusDays(1), "buritto", 1200);
    public static final Meal MEAL_4_R1_TODAY = new Meal(MEAL_ID + 6, LocalDate.now(), "bulmeni", 700);
    public static final Meal MEAL_5_R1_TODAY = new Meal(MEAL_ID + 7, LocalDate.now(), "ragu", 1200);
    public static final Meal MEAL_6_R1_TODAY = new Meal(MEAL_ID + 8, LocalDate.now(), "cotleties", 1000);
    public static final Meal MEAL_4_R2_TODAY = new Meal(MEAL_ID + 9, LocalDate.now(), "midiies", 2200);
    public static final Meal MEAL_5_R2_TODAY = new Meal(MEAL_ID + 10, LocalDate.now(), "shrimps", 1700);
    public static final Meal MEAL_6_R2_TODAY = new Meal(MEAL_ID + 11, LocalDate.now(), "calmar", 1200);
    public static final Meal MEAL_7_TMRW = new Meal(MEAL_ID + 12, LocalDate.now().plusDays(1), "porridge with pork", 1700);
    public static final Meal MEAL_8_TMRW = new Meal(MEAL_ID + 13, LocalDate.now().plusDays(1), "pasta with cheese", 900);
    public static final Meal MEAL_9_TMRW = new Meal(MEAL_ID + 14, LocalDate.now().plusDays(1), "fried vegatables", 1200);
    public static final List<Meal> R1_MENU_YSTRD = List.of(MEAL_1_R1_YSTRD, MEAL_2_R1_YSTRD, MEAL_3_R1_YSTRD);
    public static final List<Meal> R2_MENU_YSTRD = List.of(MEAL_1_R2_YSTRD, MEAL_2_R2_YSTRD, MEAL_3_R2_YSTRD);
    public static final List<Meal> R1_MENU_TODAY = List.of(MEAL_4_R1_TODAY, MEAL_5_R1_TODAY, MEAL_6_R1_TODAY);
    public static final List<Meal> R2_MENU_TODAY = List.of(MEAL_4_R2_TODAY, MEAL_5_R2_TODAY, MEAL_6_R2_TODAY);
    public static final List<Meal> MENU_TODAY = List.of(MEAL_4_R1_TODAY, MEAL_5_R1_TODAY, MEAL_6_R1_TODAY, MEAL_4_R2_TODAY, MEAL_5_R2_TODAY, MEAL_6_R2_TODAY);
    public static final List<Meal> R1_MENU_ALL = List.of(MEAL_1_R1_YSTRD, MEAL_2_R1_YSTRD, MEAL_3_R1_YSTRD, MEAL_4_R1_TODAY, MEAL_5_R1_TODAY, MEAL_6_R1_TODAY);
    public static final List<Meal> MENU_TMRW = List.of(MEAL_7_TMRW, MEAL_8_TMRW, MEAL_9_TMRW);

    public static Meal getNewMeal() {
        return new Meal(null, RESTAURANT_1, LocalDate.now(), "newFood", 666);
    }

    public static List<Meal> getTomorrowMeals() {
        return List.of(new Meal(null, RESTAURANT_1, LocalDate.now().plusDays(1), "porridge with pork", 1700),
                new Meal(null, RESTAURANT_1, LocalDate.now().plusDays(1), "pasta with cheese", 900),
                new Meal(null, RESTAURANT_1, LocalDate.now().plusDays(1), "fried vegatables", 1200));
    }

    public static Meal getUpdatedMeal(Meal old) {
        Meal updated = new Meal();
        updated.setId(old.getId());
        updated.setDescription("updatedName");
        updated.setMenuDate(LocalDate.of(2000, 1, 2));
        updated.setPrice(555);
        updated.setRestaurant(old.getRestaurant());
        return updated;
    }
}
