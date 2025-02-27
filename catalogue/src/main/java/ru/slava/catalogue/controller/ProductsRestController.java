package ru.slava.catalogue.controller;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import ru.slava.catalogue.controller.payload.NewProductPayload;
import ru.slava.catalogue.entity.Product;
import ru.slava.catalogue.service.ProductService;

@RestController
@RequestMapping("catalogue-api/products")
public class ProductsRestController {
    private final ProductService productService;
    private final MessageSource messageSource;

    @Autowired
    public ProductsRestController(ProductService productService, MessageSource messageSource) {
        this.productService = productService;
        this.messageSource = messageSource;
    }

    @GetMapping
    public List<Product> findProducts() {
        return this.productService.findAllProducts();
    }

    //consumes = MediaType.APPLICATION_JSON_VALUE это то тип тело запроса в данный момент это json
    @PostMapping
    public ResponseEntity<?> createProduct(
        @Valid @RequestBody NewProductPayload payload,
        BindingResult bindingResult,
        UriComponentsBuilder uriComponentsBuilder,
        Locale Locale
    ) throws BindException {
        if(bindingResult.hasErrors()) {
            if(bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException(bindingResult);
            }
        } else {
            Product product = this.productService.createProduct(payload.title(), payload.details());
            return ResponseEntity
                        .created(uriComponentsBuilder
                            .replacePath("/catalogue-api/products/{productId}")
                            .build(Map.of("productId", product.getId())))
                        .body(product);
        }
    }
}
