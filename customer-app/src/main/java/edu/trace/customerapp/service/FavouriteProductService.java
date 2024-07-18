package edu.trace.customerapp.service;

import edu.trace.customerapp.entity.FavouriteProduct;
import edu.trace.customerapp.entity.Product;
import reactor.core.publisher.Mono;

public interface FavouriteProductService {
    Mono<FavouriteProduct> addProductToFavourites (int productId);
    Mono<Void> removeProductFromFavourite (int productId);
    Mono<FavouriteProduct> findFavouriteProductByProduct(int productId);
}
