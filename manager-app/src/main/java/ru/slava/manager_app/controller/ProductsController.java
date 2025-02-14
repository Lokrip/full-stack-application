package ru.slava.manager_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ru.slava.manager_app.controller.payload.NewProductPayload;
import ru.slava.manager_app.entity.Product;
import ru.slava.manager_app.service.ProductService;

@Controller
@RequestMapping("catalogue/products")
public class ProductsController {
    private final ProductService productService;


    @Autowired
    public ProductsController(ProductService productService) {
        this.productService = productService;
    }


    @GetMapping("/list")
    public String getProductsList(Model model) {
        model.addAttribute("products", this.productService.findAllProducts());
        return "catalogue/products/list";
    }

    @GetMapping("/create")
    public String getNewProductPage() {
        return "catalogue/products/new_product";
    }

    @PostMapping("/create")
    public String createProduct(NewProductPayload payload) {
        Product product = this.productService.createProduct(payload.title(), payload.details());
        return "redirect:/catalogue/products/%d".formatted(product.getId());
    }
}
