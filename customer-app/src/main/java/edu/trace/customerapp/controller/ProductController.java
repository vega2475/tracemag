package edu.trace.customerapp.controller;

import edu.trace.customerapp.client.ProductsClient;
import edu.trace.customerapp.entity.FavouriteProduct;
import edu.trace.customerapp.entity.Product;
import edu.trace.customerapp.service.FavouriteProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer/products/{productId:\\d+}")
public class ProductController {
    private final ProductsClient productsClient;
    private final FavouriteProductService favouriteProductService;

    @ModelAttribute(name = "product", binding = false)
    public Mono<Product> loadProduct(@PathVariable(name = "productId") int id) {
        return this.productsClient.findProduct(id);
    }

    @GetMapping
    public Mono<String> getProductPage(Model model, @PathVariable(name = "productId") int id){
        model.addAttribute("inFavourites", false);
        return favouriteProductService.findFavouriteProductByProduct(id).
                doOnNext(favouriteProduct -> model.addAttribute("inFavourites", true))
                .thenReturn("customer/products/product");
    }

    @PostMapping("/add-to-favourites")
    public Mono<String> addProductToFavourites(@ModelAttribute Mono<Product> productMono){
        return productMono.
                map(Product::id)
                .flatMap(productId -> this.favouriteProductService.addProductToFavourites(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId)));

    }

    @PostMapping("/delete-from-favourites")
    public Mono<String> deleteProductFromFavourites(@ModelAttribute Mono<Product> productMono){
        return productMono.
                map(Product::id)
                .flatMap(productId -> this.favouriteProductService.removeProductFromFavourite(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId)));

    }
}
