package edu.trace.feedbackservice.service;


import edu.trace.feedbackservice.entity.FavouriteProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FavouriteProductService {
    Mono<FavouriteProduct> addProductToFavourites (int productId, String userId);
    Mono<Void> removeProductFromFavourite (int productId, String userId);
    Mono<FavouriteProduct> findFavouriteProductByProduct(int productId, String userId);
    Flux<FavouriteProduct> findFavouriteProducts(String userId);

}
