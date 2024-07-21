package edu.trace.customerapp.client;

import edu.trace.customerapp.client.exception.ClientBadRequestException;
import edu.trace.customerapp.client.payload.NewFavouriteProductPayload;
import edu.trace.customerapp.entity.FavouriteProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class WebClientFavouriteProductsClient implements FavouriteProductsClient {
    private final WebClient webClient;
    @Override
    public Mono<FavouriteProduct> findFavouriteProductByProductId(int productId) {
        return webClient.get().uri("feedback-api/favourite/products/by-product-id/{productId}",
                productId).retrieve().bodyToMono(FavouriteProduct.class)
                .onErrorComplete(WebClientResponseException.NotFound.class);
    }

    @Override
    public Mono<FavouriteProduct> addProductToFavourites(int productId) {
        return webClient.post().uri("/feedback-api/favourite/products")
                .bodyValue(new NewFavouriteProductPayload(productId))
                .retrieve().bodyToMono(FavouriteProduct.class)
                .onErrorMap(WebClientResponseException.BadRequest.class, exception ->
                        new ClientBadRequestException(exception.getCause(),
                                ((List<String>)exception.getResponseBodyAs(ProblemDetail.class).getProperties().get("errors"))));
    }

    @Override
    public Mono<Void> removeProductFromFavourite(int productId) {
        return webClient.delete()
                .uri("/feedback-api/favourite/products/by-product-id/{productId}", productId)
                .retrieve()
                .toBodilessEntity()
                .then();
    }

    @Override
    public Flux<FavouriteProduct> findFavouriteProducts() {
        return webClient.get().uri("/feedback-api/favourite/products").retrieve().bodyToFlux(FavouriteProduct.class);
    }
}
