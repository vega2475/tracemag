package edu.trace.customerapp.config;

import edu.trace.customerapp.client.WebClientFavouriteProductsClient;
import edu.trace.customerapp.client.WebClientProductReviewsClient;
import edu.trace.customerapp.client.WebClientProductsClient;
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

    @Bean
    public WebClientFavouriteProductsClient webClientFavouriteProductsClient(){
        return new WebClientFavouriteProductsClient(WebClient.builder()
                .baseUrl("http://localhost:8083")
                .build());
    }

    @Bean
    public WebClientProductReviewsClient webClientProductReviewsClient(){
        return new WebClientProductReviewsClient(WebClient.builder()
                .baseUrl("http://localhost:8083")
                .build());
    }
}
