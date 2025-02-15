package ru.slava.manager_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
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
    //при использование @Valid внутри NewProductPayload будут сработаны валидаторы
    //и результат валидаций попадет в bindingResult
    public String createProduct(@Valid NewProductPayload payload,
                                BindingResult bindingResult,
                                Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", bindingResult.getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .toList());
                
            return "catalogue/products/new_product";
        } else {
            Product product = this.productService.createProduct(payload.title(), payload.details());
            return "redirect:/catalogue/products/%d".formatted(product.getId());
        }
    }
}
