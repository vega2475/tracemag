package edu.trace.feedbackservice.controller;

import edu.trace.feedbackservice.controller.payload.NewProductReviewPayload;
import edu.trace.feedbackservice.entity.FavouriteProduct;
import edu.trace.feedbackservice.service.FavouriteProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/feedback-api/favourite/products")
@RequiredArgsConstructor
public class FavouritesProductsController {
    private final FavouriteProductService favouriteProductService;

    @GetMapping
    public Flux<FavouriteProduct> findFavouriteProducts(Mono<JwtAuthenticationToken> authenticationTokenMono){
        return authenticationTokenMono.flatMapMany(token ->  favouriteProductService.findFavouriteProducts(token.getToken().getSubject()));
    }

    @GetMapping("by-product-id/{productId:\\d+}")
    public Mono<FavouriteProduct> findFavouriteProductByProductId(@PathVariable (name = "productId") int productId,
                                                                  Mono<JwtAuthenticationToken> authenticationTokenMono){
        return authenticationTokenMono.flatMap(token ->  favouriteProductService.findFavouriteProductByProduct(productId, token.getToken().getSubject()));
    }

    @PostMapping
    public Mono<ResponseEntity<FavouriteProduct>> createFavouriteProduct(@Valid @RequestBody Mono<NewProductReviewPayload> payloadMono,
                                                                         UriComponentsBuilder componentsBuilder,
                                                                  Mono<JwtAuthenticationToken> authenticationTokenMono){
        return Mono.zip(authenticationTokenMono, payloadMono).flatMap(tuple -> favouriteProductService
                    .addProductToFavourites(tuple.getT2().productId(), tuple.getT1().getToken().getSubject())
                .map(favouriteProduct -> ResponseEntity.created(componentsBuilder.replacePath("/feedback-api/favourite/products/{id}")
                                .build(favouriteProduct.getId()))
                        .body(favouriteProduct)));
    }

    @DeleteMapping("by-product-id/{productId:\\d+}")
    public Mono<ResponseEntity<Void>> removeProductFromFavourites(@PathVariable (name = "productId") int productId,
                                                                  Mono<JwtAuthenticationToken> authenticationTokenMono){
        return  authenticationTokenMono.flatMap(token ->
                favouriteProductService.removeProductFromFavourite(productId, token.getToken().getSubject())).then(Mono.just(ResponseEntity.noContent().build()));
    }
}
