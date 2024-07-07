package edu.trace.catalogue.repository;

import edu.trace.catalogue.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends CrudRepository<Product, Integer> {
    @Query(name = "Product.findAllByTitleLikeIgnoreCase")
    Iterable<Product> findAllByTitleLikeIgnoreCase(@Param("filter") String filter);
}
