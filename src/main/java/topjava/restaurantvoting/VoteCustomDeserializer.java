package topjava.restaurantvoting;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.IntNode;
import org.springframework.beans.factory.annotation.Autowired;
import topjava.restaurantvoting.model.Restaurant;
import topjava.restaurantvoting.model.User;
import topjava.restaurantvoting.model.Vote;
import topjava.restaurantvoting.repository.RestaurantRepository;
import topjava.restaurantvoting.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDate;

public class VoteCustomDeserializer extends StdDeserializer<Vote> {
    @Autowired
    UserRepository ur;
    @Autowired
    RestaurantRepository rr;
    public VoteCustomDeserializer() {
        this(null);
    }

    public VoteCustomDeserializer(Class<?> v) {
        super(v);
    }

    @Override
    public Vote deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {
        JsonNode node = jp.getCodec().readTree(jp);

        Integer id = node.has("id")? (Integer) ((IntNode) node.get("id")).numberValue():null;
        LocalDate voteDate = LocalDate.parse(node.get("voteDate").asText());
        User user =  ur.findById((Integer)node.get("user").numberValue()).orElseThrow();
        Restaurant restaurant = rr.findById((Integer)node.get("restaurant").numberValue()).orElseThrow();

        return new Vote(id, voteDate, user,restaurant);
    }
}
