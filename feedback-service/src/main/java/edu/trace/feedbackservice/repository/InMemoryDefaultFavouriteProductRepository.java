package edu.trace.feedbackservice.repository;

import edu.trace.feedbackservice.entity.FavouriteProduct;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
@Repository
public class InMemoryDefaultFavouriteProductRepository implements FavouriteProductRepository {
    private final List<FavouriteProduct> favouriteProductList = Collections.synchronizedList(new LinkedList<>());
    @Override
    public Mono<FavouriteProduct> save(FavouriteProduct favouriteProduct) {
        this.favouriteProductList.add(favouriteProduct);
        return Mono.just(favouriteProduct);
    }

    @Override
    public Mono<Void> deleteByProductId(int productId) {
        this.favouriteProductList.removeIf(favouriteProduct -> favouriteProduct.getProductId() == productId);
        return Mono.empty();
    }

    @Override
    public Mono<FavouriteProduct> findByProductId(int productId) {
        return Flux.fromIterable(this.favouriteProductList)
                .filter(favouriteProduct -> favouriteProduct.getProductId() == productId)
                .singleOrEmpty();
    }

    @Override
    public Flux<FavouriteProduct> findAll() {
        return Flux.fromIterable(favouriteProductList);
    }
}
