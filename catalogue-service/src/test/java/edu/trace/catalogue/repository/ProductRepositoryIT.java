package edu.trace.catalogue.repository;

import edu.trace.catalogue.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Sql("/sql/products.sql")
@Transactional
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryIT {
    @Autowired
    private ProductRepository productRepository;

    @Test
    void findAllByTitleLikeIgnoreCase_ReturnFilteredProductList(){
        //given
        String filter = "%огурец%";
        //when
        Iterable<Product> products = this.productRepository.findAllByTitleLikeIgnoreCase(filter);

        //then
        assertEquals(List.of(new Product(4, "огурец", "описание огурца")), products);
    }
}