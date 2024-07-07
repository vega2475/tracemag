package edu.trace.manager.contoller;

import edu.trace.manager.client.ProductsRestClient;
import edu.trace.manager.contoller.payload.NewProductPayload;
import edu.trace.manager.entity.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/catalogue/products")
public class ProductsController {
    private final ProductsRestClient productsRestClient;

    @GetMapping(value = "list")
    public String getProductsList(Model model){
        model.addAttribute("products", this.productsRestClient.findAllProducts());
        return "catalogue/products/list";
    }

    @GetMapping("/create")
    public String getNewProductPage(){
        return "catalogue/products/new_product";
    }

    @PostMapping("/create")
    public String createProduct(NewProductPayload payload){
        Product product = this.productsRestClient.createProduct(payload.title(), payload.details());
        return "redirect:/catalogue/products/%d".formatted(product.id());
    }
}
