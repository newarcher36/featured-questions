package com.flaregames.featuredquestions.infrastructure.client.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.time.Instant.ofEpochMilli;

public class DateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode creationDate = jsonParser.getCodec().readTree(jsonParser);
        return creationDate.isNumber() ? ofEpochMilli(creationDate.asLong())
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime() : null;

    }
}
