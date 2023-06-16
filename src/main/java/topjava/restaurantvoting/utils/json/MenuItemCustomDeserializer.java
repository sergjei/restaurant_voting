package topjava.restaurantvoting.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import topjava.restaurantvoting.model.MenuItem;
import topjava.restaurantvoting.model.Restaurant;
import topjava.restaurantvoting.repository.RestaurantRepository;

import java.io.IOException;
import java.time.LocalDate;

@Configurable
public class MenuItemCustomDeserializer extends StdDeserializer<MenuItem> {
    @Autowired
    private RestaurantRepository rr;

    public MenuItemCustomDeserializer() {
        this(null);
    }

    public MenuItemCustomDeserializer(Class<MenuItem> v) {
        super(v);
    }

    @Override
    public MenuItem deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        Integer id = node.has("id") ? (Integer) (node.get("id")).numberValue() : null;
        Integer restaurantId = node.has("restaurant") ? (Integer) node.get("restaurant").numberValue() : null;
        Restaurant restaurant = restaurantId != null ? rr.findById(restaurantId).orElseThrow(
                () -> new EntityNotFoundException("Can`t find restaurant with  id = " + restaurantId)
        ) : null;
        LocalDate menuDate = LocalDate.parse(node.get("menuDate").asText());
        String description = node.get("description").asText();
        Integer price = node.get("price").asInt();
        return new MenuItem(id, restaurant, menuDate, description, price);
    }
}
