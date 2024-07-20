package edu.trace.customerapp.client;


import edu.trace.customerapp.client.exception.ClientBadRequestException;
import edu.trace.customerapp.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class WebClientProductsClient implements ProductsClient {

    private final WebClient webClient;

    @Override
    public Flux<Product> findAllProducts(String filter) {
        return this.webClient.get().uri("/api/v1/catalogue/products?filter={filter}", filter)
                .retrieve().bodyToFlux(Product.class);
    }

    @Override
    public Mono<Product> findProduct(int id) {
        return this.webClient.get().
                uri("/api/v1/catalogue/products/{productId}", id)
                .retrieve().bodyToMono(Product.class)
                .onErrorComplete(WebClientResponseException.NotFound.class);
    }
}
