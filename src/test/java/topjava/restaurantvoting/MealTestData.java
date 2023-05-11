package topjava.restaurantvoting;

import topjava.restaurantvoting.model.Meal;

import java.time.LocalDate;
import java.util.List;

public class MealTestData {
    public static final MatcherFactory.Matcher<Meal> MEAL_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Meal.class, "restaurant");
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
    public static final Meal MEAL_6_R2_TODAY = new Meal(MEAL_ID, LocalDate.now(), "calmar", 1200);
    public static final List<Meal> R1_MENU_YSTRD = List.of(MEAL_1_R1_YSTRD, MEAL_2_R1_YSTRD, MEAL_3_R1_YSTRD);
    public static final List<Meal> R2_MENU_YSTRD = List.of(MEAL_1_R2_YSTRD, MEAL_2_R2_YSTRD, MEAL_3_R2_YSTRD);
    public static final List<Meal> R1_MENU_TODAY = List.of(MEAL_4_R1_TODAY, MEAL_5_R1_TODAY, MEAL_6_R1_TODAY);
    public static final List<Meal> R2_MENU_TODAY = List.of(MEAL_4_R2_TODAY, MEAL_5_R2_TODAY, MEAL_6_R2_TODAY);

    public static Meal getNew() {
        return new Meal(null, RestaurantTestData.RESTAURANT_1, "newFood", LocalDate.now(), 666);
    }

    public static Meal getUpdated(Meal old) {
        Meal updated = new Meal();
        updated.setId(old.getId());
        updated.setDescription("updatedName");
        updated.setMenuDate(LocalDate.of(2000, 1, 2));
        updated.setPrice(555);
        return updated;
    }
}
