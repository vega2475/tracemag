package edu.trace.feedbackservice.repository;

import edu.trace.feedbackservice.entity.ProductReview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductReviewRepository {
    Flux<ProductReview> findProductReviewsByProductId(int productId);
    Mono<ProductReview> save(ProductReview productReview);
}
