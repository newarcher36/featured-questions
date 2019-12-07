package com.flaregames.featuredquestions.infrastructure.client.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;

@Configuration
public class HttpClientConfiguration {

    @Bean
    public HttpClient anHttpClient() {
        return HttpClient.newBuilder().build();
    }
}
