package com.redis.redi2read.cofig;

import com.redislabs.lettusearch.RediSearchClient;
import com.redislabs.lettusearch.StatefulRediSearchConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RediSearchConfig {

    @Bean
    public RediSearchClient rediSearchClient() {
        return RediSearchClient.create("redis://localhost:6379"); // Replace with your Redis URL
    }

    @Bean
    public StatefulRediSearchConnection<String, String> statefulRediSearchConnection(RediSearchClient rediSearchClient) {
        return rediSearchClient.connect();
    }
}

