package edu.trace.catalogue.service;

import edu.trace.catalogue.entity.Product;
import edu.trace.catalogue.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DefaultProductService implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public Iterable<Product> findAllProducts(String filter) {
        if (filter != null && !filter.isBlank()){
            return this.productRepository.findAllByTitleLikeIgnoreCase("%" + filter + "%");
        }
        return this.productRepository.findAll();
    }

    @Override
    @Transactional
    public Product createProduct(String title, String details) {
        return this.productRepository.save(new Product(null, title, details));
    }

    @Override
    public Optional<Product> findById(int productId) {
        return productRepository.findById(productId);
    }

    @Override
    @Transactional
    public void updateProduct(Integer id, String title, String details) {
        this.productRepository.findById(id).ifPresentOrElse(
                product -> {
                    product.setTitle(title);
                    product.setDetails(details);
                }, () -> {
                    throw new NoSuchElementException();
                }
        );
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        this.productRepository.deleteById(id);
    }
}
