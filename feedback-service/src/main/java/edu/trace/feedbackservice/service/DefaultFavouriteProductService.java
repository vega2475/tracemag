package edu.trace.feedbackservice.service;

import edu.trace.feedbackservice.entity.FavouriteProduct;
import edu.trace.feedbackservice.repository.FavouriteProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
@Service
@RequiredArgsConstructor
public class DefaultFavouriteProductService implements FavouriteProductService {
    private final FavouriteProductRepository favouriteProductRepository;
    @Override
    public Mono<FavouriteProduct> addProductToFavourites(int productId, String userId) {
        FavouriteProduct favouriteProduct = new FavouriteProduct(UUID.randomUUID(), productId, userId);
        return this.favouriteProductRepository.save(favouriteProduct);
    }

    @Override
    public Mono<Void> removeProductFromFavourite(int productId, String userId) {
        return this.favouriteProductRepository.deleteByProductIdAndUserId(productId, userId);
    }

    @Override
    public Mono<FavouriteProduct> findFavouriteProductByProduct(int productId, String userId) {
        return this.favouriteProductRepository.findByProductIdAndUserId(productId, userId);
    }

    @Override
    public Flux<FavouriteProduct> findFavouriteProducts(String userId) {
        return this.favouriteProductRepository.findAllByUserId(userId);
    }
}
