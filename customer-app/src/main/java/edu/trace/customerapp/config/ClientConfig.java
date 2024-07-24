package edu.trace.customerapp.config;

import edu.trace.customerapp.client.WebClientFavouriteProductsClient;
import edu.trace.customerapp.client.WebClientProductReviewsClient;
import edu.trace.customerapp.client.WebClientProductsClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {

    @Bean
    @Scope("prototype")
    public WebClient.Builder tracemagServicesWebClientBuilder(
            ServerOAuth2AuthorizedClientRepository authorizedClientRepository,
            ReactiveClientRegistrationRepository clientRegistrationRepository

    ){
        ServerOAuth2AuthorizedClientExchangeFilterFunction filterFunction = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                        clientRegistrationRepository, authorizedClientRepository
                );

        filterFunction.setDefaultClientRegistrationId("keycloak");

        return  WebClient.builder()
                .filter(filterFunction);
    }

    @Bean
    public WebClientProductsClient webClientProductsClient(WebClient.Builder tracemagServicesWebClientBuilder){
        return new WebClientProductsClient(tracemagServicesWebClientBuilder
                .baseUrl("http://localhost:8081")
                .build());
    }

    @Bean
    public WebClientFavouriteProductsClient webClientFavouriteProductsClient(WebClient.Builder tracemagServicesWebClientBuilder){
        return new WebClientFavouriteProductsClient(tracemagServicesWebClientBuilder
                .baseUrl("http://localhost:8083")
                .build());
    }

    @Bean
    public WebClientProductReviewsClient webClientProductReviewsClient(WebClient.Builder tracemagServicesWebClientBuilder){
        return new WebClientProductReviewsClient(tracemagServicesWebClientBuilder
                .baseUrl("http://localhost:8083")
                .build());
    }
}
