package ru.slava.customer_app.controller;

import java.security.PublicKey;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import reactor.core.publisher.Mono;
import ru.slava.customer_app.client.ProductsClient;
import ru.slava.customer_app.entity.Product;

@Controller
@RequestMapping(value = "customer/products/{productId:\\d+}")
public class ProductController {

    private final ProductsClient productsClient;


    public ProductController(ProductsClient productsClient) {
        this.productsClient = productsClient;
    }

    @ModelAttribute(name = "product", binding = false)
    public Mono<Product> loadProduct(@PathVariable("productId") int id) {
        return this.productsClient.findProduct(id);
    }

    @GetMapping
    public String getProductPage() {
        return "customer/products/product";
    }
}
