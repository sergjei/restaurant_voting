package topjava.restaurantvoting.utils.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import topjava.restaurantvoting.model.MenuItem;

import java.io.IOException;

public class MenuItemCustomSerializer extends StdSerializer<MenuItem> {

    public MenuItemCustomSerializer() {
        this(null);
    }

    public MenuItemCustomSerializer(Class<MenuItem> v) {
        super(v);
    }

    @Override
    public void serialize(
            MenuItem value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        jgen.writeStartObject();
        if (!value.isNew()) {
            jgen.writeNumberField("id", value.getId());
        }
        if (value.getRestaurant() != null) {
            jgen.writeNumberField("restaurant", value.getRestaurant().getId());
        }
        jgen.writeStringField("name", value.getName());
        jgen.writeStringField("menuDate", value.getMenuDate().toString());
        jgen.writeNumberField("price", value.getPrice());
        jgen.writeEndObject();
    }
}
