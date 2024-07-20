package edu.trace.customerapp.client;

import edu.trace.customerapp.entity.FavouriteProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FavouriteProductsClient {

    Mono<FavouriteProduct> findFavouriteProductByProductId(int productId);

    Mono<FavouriteProduct> addProductToFavourites(int productId);

    Mono<Void> removeProductFromFavourite(int productId);

    Flux<FavouriteProduct> findFavouriteProducts();
}
