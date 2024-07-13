package edu.trace.catalogue.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;

@Configuration
public class TestingBeans {
    @Bean
    public JwtDecoder jwtDecoder(){
        return Mockito.mock(JwtDecoder.class);
    }
}
