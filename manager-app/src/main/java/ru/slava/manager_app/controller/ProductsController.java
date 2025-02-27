package ru.slava.manager_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.slava.manager_app.client.ProductRestClient;
import ru.slava.manager_app.controller.payload.NewProductPayload;
import ru.slava.manager_app.entity.Product;
import ru.slava.manager_app.exeption.BadRequestExeption;

@Controller
@RequestMapping("catalogue/products")
public class ProductsController {
    private final ProductRestClient productRestClient;


    @Autowired
    public ProductsController(ProductRestClient productRestClient) {
        this.productRestClient = productRestClient;
    }


    @GetMapping("/list")
    public String getProductsList(Model model) {
        model.addAttribute("products", this.productRestClient.findAllProducts());
        return "catalogue/products/list";
    }

    @GetMapping("/create")
    public String getNewProductPage() {
        return "catalogue/products/new_product";
    }

    @PostMapping("/create")
    //при использование @Valid внутри NewProductPayload будут сработаны валидаторы
    //и результат валидаций попадет в bindingResult
    public String createProduct(NewProductPayload payload,
                                Model model) {
        try {
            Product product = this.productRestClient.createProduct(payload.title(), payload.details());
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch (BadRequestExeption exception) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exception.getErrors());
            return "catalogue/products/new_product";
        }

    }
}
