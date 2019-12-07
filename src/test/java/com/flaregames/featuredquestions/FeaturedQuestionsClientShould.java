package com.flaregames.featuredquestions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.flaregames.featuredquestions.infrastructure.client.FeaturedQuestionsClient;
import com.flaregames.featuredquestions.infrastructure.client.configuration.StackOverflowProperties;
import com.flaregames.featuredquestions.infrastructure.client.dto.QuestionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Scanner;

import static java.time.Instant.ofEpochMilli;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class FeaturedQuestionsClientShould {

	private static final String TEST_URL = "https://test.com";
	private FeaturedQuestionsClient featuredQuestionsClient;

	@Mock
	private HttpClient httpClient;

	@Mock
	private StackOverflowProperties properties;

	@Mock
	private HttpResponse<String> response;

	@BeforeEach void
	init() {
		featuredQuestionsClient = new FeaturedQuestionsClient(httpClient, properties);
	}

	@Test void
	get_the_newest_questions_from_stackoverflow() throws IOException, InterruptedException {

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(TEST_URL))
				.GET()
				.build();

		String jsonResponse = loadJsonResponse();

		given(properties.getFeaturedQuestionsUrl()).willReturn(TEST_URL);
		given(response.body()).willReturn(jsonResponse);
		given(httpClient.send(request, HttpResponse.BodyHandlers.ofString())).willReturn(response);

		List<QuestionDto> questions = featuredQuestionsClient.getFeaturedQuestions();

		assertThat(questions.get(0))
				.usingRecursiveComparison()
				.isEqualTo(aQuestionDto());
	}
	
	@Test void
	empty_list_when_json_response_is_blank() throws IOException, InterruptedException {

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(TEST_URL))
				.GET()
				.build();

		given(properties.getFeaturedQuestionsUrl()).willReturn(TEST_URL);
		given(response.body()).willReturn("");
		given(httpClient.send(request, HttpResponse.BodyHandlers.ofString())).willReturn(response);

		List<QuestionDto> questions = featuredQuestionsClient.getFeaturedQuestions();

		assertThat(questions)
				.isEmpty();
	}

	@Test void
	empty_list_when_featured_questions_request_fail() throws IOException, InterruptedException {

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(TEST_URL))
				.GET()
				.build();

		given(properties.getFeaturedQuestionsUrl()).willReturn(TEST_URL);
		given(httpClient.send(request, HttpResponse.BodyHandlers.ofString())).willThrow(new InterruptedException());

		List<QuestionDto> questions = featuredQuestionsClient.getFeaturedQuestions();

		assertThat(questions)
				.isEmpty();
	}

	@Test void
	empty_list_when_malformed_json() throws IOException, InterruptedException {

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(TEST_URL))
				.GET()
				.build();

		given(properties.getFeaturedQuestionsUrl()).willReturn(TEST_URL);
		given(response.body()).willReturn("{\"abc:\"");
		given(httpClient.send(request, HttpResponse.BodyHandlers.ofString())).willReturn(response);

		List<QuestionDto> questions = featuredQuestionsClient.getFeaturedQuestions();

		assertThat(questions)
				.isEmpty();
	}

	private String loadJsonResponse() {
		InputStream is = getClass().getResourceAsStream("/questions.json");
		try (Scanner scanner = new Scanner(is, StandardCharsets.UTF_8.name())) {
			return scanner.useDelimiter("\\A").next();
		}
	}

	private QuestionDto aQuestionDto() throws JsonProcessingException {
		QuestionDto questionDto = new QuestionDto();
		questionDto.setId(1L);
		questionDto.setTags(List.of("html"));
		questionDto.setAnswered(true);
		questionDto.setViewCount(1);
		questionDto.setAnswerCount(1);
		questionDto.setCreationDate(aLocalDateTime());
		questionDto.setUserId(3L);
		return questionDto;
	}

	private LocalDateTime aLocalDateTime() {
		return ofEpochMilli(1575307766)
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
	}
}