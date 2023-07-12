package com.github.sergjei.restaurant_voting.utils.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.sergjei.restaurant_voting.config.WebSecurityConfig;

import java.io.IOException;

public class PasswordDeserializer extends JsonDeserializer<String> {
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectCodec oc = jsonParser.getCodec();
        JsonNode node = oc.readTree(jsonParser);
        String rawPassword = node.asText();
        return WebSecurityConfig.ENCODER.encode(rawPassword);
    }
}
