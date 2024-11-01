package com.example.filmpass.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Bean
    fun webClient(): WebClient {
        return WebClient.builder()
            .baseUrl("http://www.kobis.or.kr/kobisopenapi/webservice/rest")
            .build()
    }

    @Bean
    fun webClientKMDB(): WebClient {
        return WebClient.builder()
            .baseUrl("http://api.koreafilm.or.kr")
            .build()
    }
}
