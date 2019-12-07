package com.flaregames.featuredquestions.infrastructure.client.configuration;

import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;

@Named
public class StackOverflowProperties {

    @Value("${stackoverflow.urls.featured-questions}")
    private String featuredQuestionsUrl;

    public String getFeaturedQuestionsUrl() {
        return featuredQuestionsUrl;
    }
}
