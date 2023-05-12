package topjava.restaurantvoting.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import topjava.restaurantvoting.model.Meal;

import java.io.IOException;

public class MealCustomSerializer extends StdSerializer<Meal> {

    public MealCustomSerializer() {
        this(null);
    }

    public MealCustomSerializer(Class<Meal> v) {
        super(v);
    }

    @Override
    public void serialize(
            Meal value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        jgen.writeStartObject();
        if (!value.isNew()) {
            jgen.writeNumberField("id", value.getId());
        }
        if (value.getRestaurant() != null) {
            jgen.writeNumberField("restaurant", value.getRestaurant().getId());
        }
        jgen.writeStringField("description", value.getDescription());
        jgen.writeStringField("menuDate", value.getMenuDate().toString());
        jgen.writeNumberField("price", value.getPrice());
        jgen.writeEndObject();
    }
}
