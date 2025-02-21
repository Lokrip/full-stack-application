package ru.slava.catalogue.controller;

import java.net.BindException;
import java.util.Locale;
import java.util.NoSuchElementException;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import ru.slava.catalogue.controller.payload.UpdateProductPayload;
import ru.slava.catalogue.entity.Product;
import ru.slava.catalogue.service.ProductService;

@RestController
@RequestMapping("catalogue-api/products/{productId:\\d}")
public class ProductRestController {
    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @ModelAttribute("product")
    public Product getProduct(@PathVariable("productId") int productId) {
        return this.productService.findProduct(productId)
            .orElseThrow(() -> new NoSuchElementException("catalogue.errors.product.not_found"));
    }

    @GetMapping
    public Product findProduct(@ModelAttribute(name = "product", binding = false) Product product) {
        return product;
    }


    @PatchMapping
    public ResponseEntity<?> updateProduct(
        @PathVariable("productId") int productId,
        @Valid @RequestBody UpdateProductPayload payload,
        BindingResult bindingResult
    ) throws BindException {
        if(bindingResult.hasErrors()) {
            if(bindingResult instanceof BindException exception) {
                throw exception;
            } else {
                throw new BindException();
            }
        } else {
            productService.updateProduct(productId, payload.title(), payload.details());
            return ResponseEntity.noContent().build();
        }
    }


    @DeleteMapping
    public ResponseEntity<Void> deleteProduct(@PathVariable int productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
}
