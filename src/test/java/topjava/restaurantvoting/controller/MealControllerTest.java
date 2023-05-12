package topjava.restaurantvoting.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import topjava.restaurantvoting.model.Meal;
import topjava.restaurantvoting.repository.MealRepository;
import topjava.restaurantvoting.utils.json.JsonUtil;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static topjava.restaurantvoting.MealTestData.*;
import static topjava.restaurantvoting.RestaurantTestData.RESTAURANT_ID;
import static topjava.restaurantvoting.UserTestData.ADMIN_EMAIL;

class MealControllerTest extends AbstractControllerTest {
    public static final String CURRENT_URL = "/rest/admin/restaurant/{rest_id}/meal";
    @Autowired
    private MealRepository mealRepository;

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(CURRENT_URL, RESTAURANT_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(R1_MENU_ALL));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void getToday() throws Exception {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("startDate", LocalDate.now().toString());
        parameters.add("endDate", LocalDate.now().toString());
        perform(MockMvcRequestBuilders.get(CURRENT_URL, RESTAURANT_ID)
                .params(parameters))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(R1_MENU_TODAY));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(CURRENT_URL + "/{id}", RESTAURANT_ID, MEAL_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(MEAL_1_R1_YSTRD));
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(CURRENT_URL + "/{id}", RESTAURANT_ID, MEAL_ID + 1))
                .andExpect(status().isNoContent());
        MEAL_MATCHER.assertMatch(
                mealRepository.getByRestaurantAndDateInclusive(RESTAURANT_ID, LocalDate.now().minusDays(1), LocalDate.now()),
                List.of(MEAL_1_R1_YSTRD, MEAL_3_R1_YSTRD, MEAL_4_R1_TODAY, MEAL_5_R1_TODAY, MEAL_6_R1_TODAY)
        );
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void update() throws Exception {
        Meal updated = getUpdatedMeal(mealRepository.findById(MEAL_ID).orElseThrow());
        perform(MockMvcRequestBuilders.put(CURRENT_URL + "/{id}", RESTAURANT_ID, MEAL_ID)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MEAL_MATCHER.assertMatch(mealRepository.findById(MEAL_ID).orElseThrow(), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void createOne() throws Exception {
        Meal newOne = getNewMeal();
        ResultActions action = perform(MockMvcRequestBuilders.post(CURRENT_URL, RESTAURANT_ID)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.writeValue(newOne)))
                .andExpect(status().isCreated())
                .andDo(print());
        Meal created = MEAL_MATCHER.readFromJson(action);
        int id = created.getId();
        newOne.setId(id);
        System.out.println(JsonUtil.writeValue(List.of(MEAL_1_R1_YSTRD, MEAL_2_R1_YSTRD)));
        MEAL_MATCHER.assertMatch(created, newOne);
        MEAL_MATCHER.assertMatch(mealRepository.findById(id).orElseThrow(), newOne);
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void setNewMenu() throws Exception {
        List<Meal> newMenu = getTomorrowMeals();
        ResultActions action = perform(MockMvcRequestBuilders.post(CURRENT_URL + "/updateMenu", RESTAURANT_ID)
                .contentType(MediaType.APPLICATION_JSON).content(JsonUtil.writeValue(newMenu)))
                .andExpect(status().isCreated())
                .andDo(print());
        List<Meal> created = MEAL_MATCHER.readFromJsonList(action);
        for (int i = 0; i <= 2; i++) {
            newMenu.get(i).setId(created.get(i).getId());
        }
        MEAL_MATCHER.assertMatch(created, newMenu);
        MEAL_MATCHER.assertMatch(mealRepository.getByRestaurantAndDateInclusive(RESTAURANT_ID,
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(1)), newMenu);
    }
}