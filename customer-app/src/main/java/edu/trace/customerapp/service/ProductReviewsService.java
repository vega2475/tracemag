package edu.trace.customerapp.service;

import edu.trace.customerapp.entity.ProductReview;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductReviewsService {
    Mono<ProductReview> createProductReview(int productId, int rating, String review);
    Flux<ProductReview> findProductReviewForProduct(int productId);
}
