package topjava.restaurantvoting.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import topjava.restaurantvoting.model.Restaurant;
import topjava.restaurantvoting.model.User;
import topjava.restaurantvoting.model.Vote;
import topjava.restaurantvoting.repository.RestaurantRepository;
import topjava.restaurantvoting.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDate;

@Configurable
public class VoteCustomDeserializer extends StdDeserializer<Vote> {
    @Autowired
    private UserRepository ur;
    @Autowired
    private RestaurantRepository rr;

    public VoteCustomDeserializer() {
        this(null);
    }

    public VoteCustomDeserializer(Class<?> v) {
        super(v);
    }

    @Override
    public Vote deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);
        Integer id = node.has("id") ? (Integer) (node.get("id")).numberValue() : null;
        LocalDate voteDate = LocalDate.parse(node.get("voteDate").asText());
        User user = ur.findById((Integer) node.get("user").numberValue()).orElseThrow();
        Restaurant restaurant = rr.findById((Integer) node.get("restaurant").numberValue()).orElseThrow();
        return new Vote(id, voteDate, user, restaurant);
    }
}
