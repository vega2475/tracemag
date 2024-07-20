package edu.trace.feedbackservice.service;


import edu.trace.feedbackservice.entity.ProductReview;
import edu.trace.feedbackservice.repository.ProductReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DefaultProductReviewsService implements ProductReviewsService {
    private final ProductReviewRepository productReviewRepository;
    @Override
    public Mono<ProductReview> createProductReview(int productId, int rating, String review) {
        return this.productReviewRepository.save(new ProductReview(UUID.randomUUID() ,productId, rating, review));
    }

    @Override
    public Flux<ProductReview> findProductReviewForProduct(int productId) {
        return this.productReviewRepository.findProductReviewsByProductId(productId);
    }
}
