package edu.trace.customerapp.config;

import edu.trace.customerapp.client.WebClientProductsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {
    @Bean
    public WebClientProductsClient webClientProductsClient(){
        return new WebClientProductsClient(WebClient.builder()
                .baseUrl("http://localhost:8081")
                .build());
    }
}
