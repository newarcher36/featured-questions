package com.flaregames.featuredquestions.infrastructure.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "tags",
        "is_answered",
        "view_count",
        "answer_count",
        "creation_date",
        "user_id"
})
@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode
public class QuestionDto {

    private Long id;

    @JsonProperty("tags")
    private List<String> tags;

    @JsonProperty("is_answered")
    private Boolean isAnswered;

    @JsonProperty("view_count")
    private Integer viewCount;

    @JsonProperty("answer_count")
    private Integer answerCount;

    private LocalDateTime creationDate;

    private Long userId;

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("question_id")
    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Boolean getAnswered() {
        return isAnswered;
    }

    public void setAnswered(Boolean answered) {
        isAnswered = answered;
    }

    public Integer getViewCount() {
        return viewCount;
    }

    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }

    public Integer getAnswerCount() {
        return answerCount;
    }

    public void setAnswerCount(Integer answerCount) {
        this.answerCount = answerCount;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    @JsonDeserialize(using = DateTimeDeserializer.class)
    @JsonProperty("creation_date")
    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    @JsonProperty("user_id")
    public Long getUserId() {
        return userId;
    }

    @JsonDeserialize(using = UserDeserializer.class)
    @JsonProperty("owner")
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}