package com.flaregames.featuredquestions.infrastructure.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flaregames.featuredquestions.infrastructure.client.configuration.StackOverflowProperties;
import com.flaregames.featuredquestions.infrastructure.client.dto.QuestionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static java.net.http.HttpResponse.BodyHandlers;
import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;

@Named
public class FeaturedQuestionsClient {

    private final Logger logger = LoggerFactory.getLogger(FeaturedQuestionsClient.class);
    private final HttpClient httpClient;
    private final StackOverflowProperties properties;

    public FeaturedQuestionsClient(HttpClient httpClient, StackOverflowProperties properties) {
        this.httpClient = httpClient;
        this.properties = properties;
    }

    public List<QuestionDto> getFeaturedQuestions() {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(properties.getFeaturedQuestionsUrl()))
                .build();
        try {
            HttpResponse<String> response = sendRequest(request);
            String jsonResponse = response.body();
            return mapToQuestionsDto(jsonResponse);

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            logger.error("Could not request the latest featured questions: " + e);
            return emptyList();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
        return httpClient.send(request, BodyHandlers.ofString());
    }

    private List<QuestionDto> mapToQuestionsDto(String jsonResponse) throws JsonProcessingException {

        if (isNull(jsonResponse) || jsonResponse.isEmpty()) {
            return emptyList();
        }

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(jsonResponse);

        return mapper.readValue(node.get("items").toString(), new TypeReference<>(){});
    }
}
