package edu.trace.customerapp.controller;

import edu.trace.customerapp.client.FavouriteProductsClient;
import edu.trace.customerapp.client.ProductsClient;
import edu.trace.customerapp.entity.FavouriteProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customer/products")
public class ProductsController {
    private final ProductsClient productsClient;
    private final FavouriteProductsClient favouriteProductsClient;

    @GetMapping("/list")
    public Mono<String> getProductsListPage(Model model, @RequestParam(name = "filter", required = false) String filter){
        model.addAttribute("filter", filter);
        return this.productsClient.findAllProducts(filter)
                .collectList()
                .doOnNext(products -> {
                    model.addAttribute("products", products);
                })
                .thenReturn("customer/products/list");
    }
    @GetMapping("/favourites")
    public Mono<String> getFavouritesProductsPage(Model model, @RequestParam(name = "filter", required = false) String filter){
        model.addAttribute("filter", filter);
        return this.favouriteProductsClient.findFavouriteProducts()
                .map(FavouriteProduct::productId)
                .collectList().
                flatMap(favouriteProducts -> this.productsClient.findAllProducts(filter)
                        .filter(product -> favouriteProducts.contains(product.id()))
                        .collectList()
                        .doOnNext(products -> model.addAttribute("products", products))
                )
                                .thenReturn("customer/products/favourites");

    }
}
