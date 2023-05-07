package topjava.restaurantvoting;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import topjava.restaurantvoting.model.Vote;

import java.io.IOException;

public class VoteCustomSerializer extends StdSerializer<Vote> {

    public VoteCustomSerializer() {
        this(null);
    }

    public VoteCustomSerializer(Class<Vote> v) {
        super(v);
    }

    @Override
    public void serialize(
            Vote value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());
        jgen.writeStringField("voteDate", value.getVoteDate().toString());
        jgen.writeNumberField("user", value.getUser().getId());
        jgen.writeNumberField("restaurant", value.getRestaurant().getId());
        jgen.writeEndObject();
    }
}
