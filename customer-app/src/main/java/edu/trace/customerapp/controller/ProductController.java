package edu.trace.customerapp.controller;

import edu.trace.customerapp.client.FavouriteProductsClient;
import edu.trace.customerapp.client.ProductReviewsClient;
import edu.trace.customerapp.client.ProductsClient;
import edu.trace.customerapp.client.exception.ClientBadRequestException;
import edu.trace.customerapp.controller.payload.NewProductReviewPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import edu.trace.customerapp.entity.Product;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer/products/{productId:\\d+}")
@Slf4j
public class ProductController {
    private final ProductsClient productsClient;
    private final FavouriteProductsClient favouriteProductsClient;
    private final ProductReviewsClient productReviewsClient;

    @ModelAttribute(name = "product", binding = false)
    public Mono<Product> loadProduct(@PathVariable(name = "productId") int id) {
        return this.productsClient.findProduct(id)
                .switchIfEmpty(Mono.error(new NoSuchElementException("Product Doesnt Exists")));
    }

    @GetMapping
    public Mono<String> getProductPage(Model model, @PathVariable(name = "productId") int id){
        model.addAttribute("inFavourites", false);
        return  this.productReviewsClient.findProductReviewsByProductId(id)
                        .collectList()
                                .doOnNext(productReviews -> model.addAttribute("reviews", productReviews))
                                        .then(
                favouriteProductsClient.findFavouriteProductByProductId(id).
                doOnNext(favouriteProduct -> model.addAttribute("inFavourites", true))
                .thenReturn("customer/products/product"));
    }

    @PostMapping("/add-to-favourites")
    public Mono<String> addProductToFavourites(@ModelAttribute("product") Mono<Product> productMono){
        return productMono.
                map(Product::id)
                .flatMap(productId -> this.favouriteProductsClient.addProductToFavourites(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId))
                .onErrorResume(exception -> {
                    log.error(exception.getMessage(), exception);
                    return Mono.just("redirect:/customer/products/%d".formatted(productId));
                }));

    }

    @PostMapping("/delete-from-favourites")
    public Mono<String> deleteProductFromFavourites(@ModelAttribute("product") Mono<Product> productMono){
        return productMono.
                map(Product::id)
                .flatMap(productId -> this.favouriteProductsClient.removeProductFromFavourite(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId)));

    }

    @PostMapping("/create-review")
    public Mono<String> createReview(
                                     @PathVariable(name = "productId") int id,
                                     NewProductReviewPayload newProductReviewPayload,
                                     Model model){
        return this.productReviewsClient.createProductReview(id, newProductReviewPayload.rating(), newProductReviewPayload.review())
                .thenReturn("redirect:/customer/products/%d".formatted(id))
                .onErrorResume(ClientBadRequestException.class, exception -> {
                    model.addAttribute("inFavourites", false);
                    model.addAttribute("payload", newProductReviewPayload);
                    model.addAttribute("errors", exception.getErrors());

            return favouriteProductsClient.findFavouriteProductByProductId(id).doOnNext(favouriteProduct -> model.addAttribute("inFavourites", true))
                .thenReturn("customer/products/product");
                });
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException exception, Model model){
        model.addAttribute("error", exception.getMessage());
        return "errors/404";
    }
}
