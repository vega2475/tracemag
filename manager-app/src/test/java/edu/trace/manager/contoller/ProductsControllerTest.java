package edu.trace.manager.contoller;

import edu.trace.manager.client.ProductsRestClient;
import edu.trace.manager.contoller.payload.NewProductPayload;
import edu.trace.manager.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Module tests in product controller")
class ProductsControllerTest {
    @Mock
    ProductsRestClient productsRestClient;
    @InjectMocks
    ProductsController productsController;

    @Test
    @DisplayName("createProduct create a new product and return the redirection to the product page")
    void createProduct_RequestIsValid_ReturnsRedirectionToProductPage(){
        //given
        NewProductPayload payload = new NewProductPayload("Новый товар", "описание нового товара");
        doReturn(new Product(1, "Новый товар", "описание нового товара")).
                when(this.productsRestClient).
                createProduct(eq("Новый товар"), eq("описание нового товара"));

        //when
        String result = this.productsController.createProduct(payload);

        //then
        assertEquals("redirect:/catalogue/products/1", result);

        verify(this.productsRestClient).createProduct("Новый товар", "описание нового товара");
        verifyNoMoreInteractions(this.productsRestClient);
    }
}