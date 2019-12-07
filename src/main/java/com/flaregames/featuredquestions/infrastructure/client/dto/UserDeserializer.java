package com.flaregames.featuredquestions.infrastructure.client.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class UserDeserializer extends JsonDeserializer<Long> {

    @Override
    public Long deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode owner = jsonParser.getCodec().readTree(jsonParser);
        JsonNode userId = owner.get("user_id");
        return userId != null ? userId.asLong() : 0;
    }
}
