package edu.trace.manager.contoller;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import edu.trace.manager.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "standalone")
@WireMockTest(httpPort = 54321)
public class ProductsControllerIT {
    @Autowired
    MockMvc mockMvc;

    @Test
    void getProductsList_ReturnsProductsListPage() throws Exception{
        //given
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/catalogue/products/list")
                .queryParam("filter", "товар")
                .with(user("Ryan_Gosling").roles("MANAGER"));

        WireMock.stubFor(WireMock.get(WireMock.urlPathMatching("/api/v1/catalogue/products"))
                        .withQueryParam("filter", WireMock.equalTo("товар"))
                .willReturn(WireMock.ok(
                        """
                                [
                                    {
                                        "id" : 1,
                                        "title" : "товар1",
                                        "details" : "details"
                                    },
                                    {
                                        "id" : 2,
                                        "title" : "товар2",
                                        "details" : "details2"
                                    }
                                ]
                                """
                ).withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        //when
        this.mockMvc.perform(requestBuilder)
        //then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("catalogue/products/list"),
                        model().attribute("filter", "товар"),
                        model().attribute("products", List.of(new Product(1, "товар1", "details"),
                                new Product(2, "товар2", "details2"))
                        )
                );
    }

    @Test
    void getNewProductPage_ReturnsProductPage() throws Exception{
        //given
        RequestBuilder manager = MockMvcRequestBuilders.get("/catalogue/products/create")
                .with(user("j.dewar").roles("MANAGER"));

        //when
        this.mockMvc.perform(manager)
        //then
                .andDo(print())
                .andExpectAll(
                        status().isOk(),
                        view().name("catalogue/products/new_product")
                );
    }
}
