package edu.trace.manager.config;

import edu.trace.manager.client.RestClientProductsRestClient;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;

@Configuration
public class TestingBeans {
    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(){
        return Mockito.mock(ClientRegistrationRepository.class);
    }

    @Bean
    public OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository(){
        return Mockito.mock(OAuth2AuthorizedClientRepository.class);
    }

    @Bean
    @Primary
    public RestClientProductsRestClient testRestClient(){
        return new RestClientProductsRestClient(RestClient.builder()
                .baseUrl("http://localhost:54321")
                .build());
    }

}
