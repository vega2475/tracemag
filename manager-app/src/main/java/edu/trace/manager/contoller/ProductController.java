package edu.trace.manager.contoller;

import edu.trace.manager.client.ProductsRestClient;
import edu.trace.manager.contoller.payload.UpdateProductPayload;
import edu.trace.manager.entity.Product;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products/{productId:\\d+}")
public class ProductController {
    private final ProductsRestClient productsRestClient;
    @GetMapping
    public String getProduct(){
        return "catalogue/products/product";
    }

    @GetMapping("edit")
    public String getProductEditPage(){
        return "catalogue/products/edit";
    }

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId){
        return this.productsRestClient.findById(productId).orElseThrow();
    }

    @PostMapping("edit")
    public String updateProduct(@ModelAttribute("product") Product product, UpdateProductPayload updateProductPayload){
        this.productsRestClient.updateProduct(product.id(), updateProductPayload.title(), updateProductPayload.details());
        return "redirect:/catalogue/products/%d".formatted(product.id());
    }

    @PostMapping("delete")
    public String deleteProduct(@ModelAttribute("product") Product product){
        this.productsRestClient.deleteProduct(product.id());
        return "redirect:/catalogue/products/list";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException noSuchElementException, Model model, HttpServletResponse httpServletResponse){
        httpServletResponse.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error", noSuchElementException.getMessage());
        return "errors/404";
    }
}
