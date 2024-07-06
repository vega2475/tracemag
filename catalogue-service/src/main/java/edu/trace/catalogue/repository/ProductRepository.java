package edu.trace.catalogue.repository;

import edu.trace.catalogue.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();

    Product save(Product product);

    Optional<Product> findById(int productId);

    void deleteById(Integer id);
}
