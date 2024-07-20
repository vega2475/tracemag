package edu.trace.feedbackservice.controller;

import edu.trace.feedbackservice.controller.payload.NewProductReviewPayload;
import edu.trace.feedbackservice.entity.ProductReview;
import edu.trace.feedbackservice.service.ProductReviewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/feedback-api/product-reviews")
@RequiredArgsConstructor
public class ProductReviewsController {
    private final ProductReviewsService productReviewsService;

    @GetMapping("by-product-id/{productId:\\d+}")
    public Flux<ProductReview> findProductReviewsByProductId(@PathVariable (name = "productId") int productId){
        return productReviewsService.findProductReviewForProduct(productId);
    }

    @PostMapping
    public Mono<ResponseEntity<?>> createProductReview(@Valid @RequestBody Mono<NewProductReviewPayload> payloadMono,
                                                       UriComponentsBuilder uriComponentsBuilder){
        return payloadMono
                .flatMap(payload -> productReviewsService.createProductReview(payload.productId(), payload.rating(), payload.review()))
                .map(productReview -> ResponseEntity.created(uriComponentsBuilder.replacePath("/feedback-api/product-reviews/{id}")
                        .build(productReview.getId())).body(productReview));
    }
}
