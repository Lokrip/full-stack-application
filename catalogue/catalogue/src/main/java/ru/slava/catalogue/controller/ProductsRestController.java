package ru.slava.catalogue.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.slava.catalogue.entity.Product;
import ru.slava.catalogue.service.ProductService;

@RestController
@RequestMapping("catalogue-api/products")
public class ProductsRestController {
    private final ProductService productService;

    @Autowired
    public ProductsRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> findProducts() {
        return this.productService.findAllProducts();
    }
}
