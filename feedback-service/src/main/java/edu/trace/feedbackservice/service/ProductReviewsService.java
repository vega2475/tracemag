package edu.trace.feedbackservice.service;


import edu.trace.feedbackservice.entity.ProductReview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductReviewsService {
    Mono<ProductReview> createProductReview(int productId, int rating, String review, String userId);
    Flux<ProductReview> findProductReviewForProduct(int productId);
}
