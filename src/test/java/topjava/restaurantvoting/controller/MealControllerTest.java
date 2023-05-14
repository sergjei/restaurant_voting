package topjava.restaurantvoting.controller;

import jakarta.persistence.EntityNotFoundException;
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
import topjava.restaurantvoting.to.MealTo;
import topjava.restaurantvoting.utils.MealsUtil;
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
    public static final String CURRENT_URL = "/rest/admin/restaurants/{rest_id}/meals";
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
        Meal updated = getUpdatedMeal(mealRepository.findById(MEAL_ID).orElseThrow(
                () -> new EntityNotFoundException("Can`t find meal with  id = " + MEAL_ID)
        ));
        perform(MockMvcRequestBuilders.put(CURRENT_URL + "/{id}", RESTAURANT_ID, MEAL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        MEAL_MATCHER.assertMatch(mealRepository.findById(MEAL_ID).orElseThrow(
                () -> new EntityNotFoundException("Can`t find meal with  id = " + MEAL_ID)
        ), updated);
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void createOne() throws Exception {
        MealTo newOne = MealsUtil.createFrom(getNewMeal());
        ResultActions action = perform(MockMvcRequestBuilders.post(CURRENT_URL, RESTAURANT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newOne)))
                .andExpect(status().isCreated())
                .andDo(print());
        MealTo created = MEAL_TO_MATCHER.readFromJson(action);
        int id = created.getId();
        newOne.setId(id);
        MEAL_TO_MATCHER.assertMatch(created, newOne);
        MEAL_TO_MATCHER.assertMatch(MealsUtil.createFrom(mealRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can`t find meal with  id = " + id)
        )), newOne);
    }

    @Test
    @WithUserDetails(value = ADMIN_EMAIL)
    void updateMenu() throws Exception {
        List<MealTo> newMenu = MealsUtil.getListTos(getTomorrowMeals());
        ResultActions action = perform(MockMvcRequestBuilders.post(CURRENT_URL + "/updateMenu", RESTAURANT_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMenu)))
                .andExpect(status().isCreated())
                .andDo(print());
        List<MealTo> created = MEAL_TO_MATCHER.readFromJsonList(action);
        for (int i = 0; i <= 2; i++) {
            newMenu.get(i).setId(created.get(i).getId());
        }
        MEAL_TO_MATCHER.assertMatch(created, newMenu);
        MEAL_TO_MATCHER.assertMatch(MealsUtil.getListTos(mealRepository.getByRestaurantAndDateInclusive(RESTAURANT_ID,
                LocalDate.now().plusDays(1), LocalDate.now().plusDays(1))), newMenu);
    }
}