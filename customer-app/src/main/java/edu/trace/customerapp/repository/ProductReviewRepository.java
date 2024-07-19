package edu.trace.customerapp.repository;

import edu.trace.customerapp.entity.Product;
import edu.trace.customerapp.entity.ProductReview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductReviewRepository {
    Flux<ProductReview> findProductReviewsByProductId(int productId);
    Mono<ProductReview> save(ProductReview productReview);
}
