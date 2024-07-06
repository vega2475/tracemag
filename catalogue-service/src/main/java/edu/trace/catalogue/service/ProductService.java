package edu.trace.catalogue.service;

import edu.trace.catalogue.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    List<Product> findAllProducts();
    Product createProduct(String title, String details);
    Optional<Product> findById(int productId);

    void updateProduct(Integer id, String title, String details);

    void deleteProduct(Integer id);
}