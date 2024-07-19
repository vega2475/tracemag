package edu.trace.customerapp.repository;

import edu.trace.customerapp.entity.Product;
import edu.trace.customerapp.entity.ProductReview;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Repository
public class InMemoryProductReviewRepository implements ProductReviewRepository {
    private final List<ProductReview> productReviews = Collections.synchronizedList(new LinkedList<>());


    @Override
    public Flux<ProductReview> findProductReviewsByProductId(int productId) {
        return Flux.fromIterable(productReviews).filter(productReview -> productReview.getProductId() == productId);
    }

    @Override
    public Mono<ProductReview> save(ProductReview productReview) {
        this.productReviews.add(productReview);
        return Mono.just(productReview);
    }
}
