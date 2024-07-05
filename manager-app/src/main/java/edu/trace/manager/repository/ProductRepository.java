package edu.trace.manager.repository;

import edu.trace.manager.entity.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findAll();

    Product save(Product product);

    Optional<Product> findById(int productId);

    void deleteById(Integer id);
}
