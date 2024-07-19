package edu.trace.customerapp.controller;

import edu.trace.customerapp.client.ProductsClient;
import edu.trace.customerapp.client.payload.NewProductReviewPayload;
import edu.trace.customerapp.entity.Product;
import edu.trace.customerapp.service.FavouriteProductService;
import edu.trace.customerapp.service.ProductReviewsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer/products/{productId:\\d+}")
public class ProductController {
    private final ProductsClient productsClient;
    private final FavouriteProductService favouriteProductService;
    private final ProductReviewsService productReviewsService;

    @ModelAttribute(name = "product", binding = false)
    public Mono<Product> loadProduct(@PathVariable(name = "productId") int id) {
        return this.productsClient.findProduct(id)
                .switchIfEmpty(Mono.error(new NoSuchElementException("Product Doesnt Exists")));
    }

    @GetMapping
    public Mono<String> getProductPage(Model model, @PathVariable(name = "productId") int id){
        model.addAttribute("inFavourites", false);
        return  this.productReviewsService.findProductReviewForProduct(id)
                        .collectList()
                                .doOnNext(productReviews -> model.addAttribute("reviews", productReviews))
                                        .then(
                favouriteProductService.findFavouriteProductByProduct(id).
                doOnNext(favouriteProduct -> model.addAttribute("inFavourites", true))
                .thenReturn("customer/products/product"));
    }

    @PostMapping("/add-to-favourites")
    public Mono<String> addProductToFavourites(@ModelAttribute("product") Mono<Product> productMono){
        return productMono.
                map(Product::id)
                .flatMap(productId -> this.favouriteProductService.addProductToFavourites(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId)));

    }

    @PostMapping("/delete-from-favourites")
    public Mono<String> deleteProductFromFavourites(@ModelAttribute("product") Mono<Product> productMono){
        return productMono.
                map(Product::id)
                .flatMap(productId -> this.favouriteProductService.removeProductFromFavourite(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId)));

    }

    @PostMapping("/create-review")
    public Mono<String> createReview(
                                     @PathVariable(name = "productId") int id,
                                     @Valid NewProductReviewPayload newProductReviewPayload,
                                     BindingResult bindingResult,
                                     Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("inFavourites", false);
            model.addAttribute("payload", newProductReviewPayload);
            model.addAttribute("errors", bindingResult.getAllErrors().stream().map(ObjectError::getDefaultMessage).toList());

            return favouriteProductService.findFavouriteProductByProduct(id).doOnNext(favouriteProduct -> model.addAttribute("inFavourites", true))
                .thenReturn("customer/products/product");
        }

        return this.productReviewsService.createProductReview(id, newProductReviewPayload.rating(), newProductReviewPayload.review())
                .thenReturn("redirect:/customer/products/%d".formatted(id));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException exception, Model model){
        model.addAttribute("error", exception.getMessage());
        return "errors/404";
    }
}
