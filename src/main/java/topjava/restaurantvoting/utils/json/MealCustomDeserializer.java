package topjava.restaurantvoting.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import topjava.restaurantvoting.model.Meal;
import topjava.restaurantvoting.model.Restaurant;
import topjava.restaurantvoting.repository.RestaurantRepository;

import java.io.IOException;
import java.time.LocalDate;

@Configurable
public class MealCustomDeserializer extends StdDeserializer<Meal> {
    @Autowired
    private RestaurantRepository rr;

    public MealCustomDeserializer() {
        this(null);
    }

    public MealCustomDeserializer(Class<Meal> v) {
        super(v);
    }

    @Override
    public Meal deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        Integer id = node.has("id") ? (Integer) (node.get("id")).numberValue() : null;
        Integer restId = node.has("restaurant") ? (Integer) node.get("restaurant").numberValue() : null;
        Restaurant restaurant = restId != null ? rr.findById(restId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find restaurant with  id = " + restId)
        ) : null;
        LocalDate menuDate = LocalDate.parse(node.get("menuDate").asText());
        String description = node.get("description").asText();
        Integer price = node.get("price").asInt();
        return new Meal(id, restaurant, menuDate, description, price);
    }
}
