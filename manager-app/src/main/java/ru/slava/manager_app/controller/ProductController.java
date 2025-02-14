package ru.slava.manager_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.slava.manager_app.controller.payload.UpdateProductPayload;
import ru.slava.manager_app.entity.Product;
import ru.slava.manager_app.service.ProductService;

@Controller
@RequestMapping("{productId:\\\\d+}")
public class ProductController {
    private final ProductService productService;


    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId) {
        return this.productService.findProduct(productId).orElseThrow();
    }

    //делаем так что мы должны получить цело численое число через \\d+
    @GetMapping
    public String getProduct() {
        return "catalogue/products/product";
    }

    @GetMapping("edit")
    public String getProductEditPage() {
        return "catalogue/products/edit";
    }

    @PostMapping("edit")
    public String updateProduct(
        @ModelAttribute("product") Product product, 
        UpdateProductPayload payload
    ) {
        this.productService.updateProduct(product.getId(), payload.title(), payload.details());
        return "redirect:/catalogue/products/%d".formatted(product.getId());
    }
}
