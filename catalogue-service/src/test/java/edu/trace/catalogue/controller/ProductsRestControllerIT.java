package edu.trace.catalogue.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductsRestControllerIT {
    @Autowired
    MockMvc mockMvc;

    @Test
    @Sql("/sql/products.sql")
    void findProduct_ReturnsProductsList() throws Exception{
        //given
        RequestBuilder mockMvcRequestBuilders = MockMvcRequestBuilders.get("/api/v1/catalogue/products")
                .param("filter", "товар")
                .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));
        //when
        this.mockMvc.perform(mockMvcRequestBuilders)
        //then
                .andDo(print())
                .andExpectAll(status().isOk(), content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON), content().
                        json("""
                                        [
                                          {"id": 1, "title": "Товар1", "details": "productDetails"},
                                          {"id": 2, "title": "Товар2", "details": "productDetails"},
                                          {"id": 3, "title": "Товар3", "details": "productDetails"}
]
"""));
    }

    @Test
    void createProduct_RequestIsValid_ReturnNewProduct () throws Exception{
        //given
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/catalogue/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                  {
                    "title" : "Товар5",
                    "details" : "Описание товара5"
                  }
                """)
                        .with(jwt().jwt(builder -> builder.claim("scope", "edit_catalogue")));

        //when
        mockMvc.perform(requestBuilder)
        //then
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        header().string(HttpHeaders.LOCATION, "http://localhost/api/v1/catalogue/products/1"),
                        content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                        content().json("""
                              {
                              "id" : 1,
                              "title" : "Товар5",
                              "details" : "Описание товара5"
                              }
                 """));
    }

    @Test
    void createProduct_UserIsNotAuthorized_Return403 () throws Exception{
        //given
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/api/v1/catalogue/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                  {
                    "title" : "Товар5",
                    "details" : "Описание товара5"
                  }
                """)
                        .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        //when
        mockMvc.perform(requestBuilder)
        //then
                .andDo(print())
                .andExpectAll(
                        status().isForbidden());
    }
}