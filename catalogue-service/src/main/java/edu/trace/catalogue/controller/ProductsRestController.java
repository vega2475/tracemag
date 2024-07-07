package edu.trace.catalogue.controller;

import edu.trace.catalogue.controller.payload.NewProductPayload;
import edu.trace.catalogue.entity.Product;
import edu.trace.catalogue.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/catalogue/products")
public class ProductsRestController {
    private final ProductService productService;
    @GetMapping
    public Iterable<Product> findProducts(@RequestParam(name = "filter", required = false) String filter){
        return this.productService.findAllProducts(filter);
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody NewProductPayload payload, UriComponentsBuilder builder, BindingResult bindingResult) throws BindException {
        if(bindingResult.hasErrors()){
            if (bindingResult instanceof BindException bindException){
                throw bindException;
            } else {
                throw new BindException(bindingResult);
            }
        }  else{
            Product product = this.productService.createProduct(payload.title(), payload.details());
        return ResponseEntity.created(
                builder.
                        replacePath("/api/v1/catalogue/products/{productId}").
                        build(Map.of("productId", product.getId()))).
                body(product);
        }
    }
}
