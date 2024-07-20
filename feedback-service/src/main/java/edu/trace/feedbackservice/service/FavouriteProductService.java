package edu.trace.feedbackservice.service;


import edu.trace.feedbackservice.entity.FavouriteProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FavouriteProductService {
    Mono<FavouriteProduct> addProductToFavourites (int productId);
    Mono<Void> removeProductFromFavourite (int productId);
    Mono<FavouriteProduct> findFavouriteProductByProduct(int productId);
    Flux<FavouriteProduct> findFavouriteProducts();
}
