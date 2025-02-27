package ru.slava.manager_app.controller;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import ru.slava.manager_app.client.ProductRestClient;
import ru.slava.manager_app.controller.payload.UpdateProductPayload;
import ru.slava.manager_app.entity.Product;
import ru.slava.manager_app.exeption.BadRequestExeption;

@Controller
@RequestMapping("catalogue/products/{productId:\\d+}")
public class ProductController {
     private final ProductRestClient productRestClient;


    @Autowired
    public ProductController(ProductRestClient productRestClient) {
        this.productRestClient = productRestClient;
    }

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId) {
        return this.productRestClient.findProduct(productId)
                //catalogue.errors.product.not_found это ключь в messages.properties тоесть мы берем данные с ключа и передаем в ошибку
                .orElseThrow(() -> new NoSuchElementException("catalogue.errors.product.not_found"));
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
        //гда Spring MVC вызывает метод с @ModelAttribute, он по умолчанию создает новый экземпляр переданного объекта (Product) и заполняет его данными из запроса
        //для того чтобы мы получали элемент с @ModelAttribute("product") this.productService.findProduct(productId)
        //а не создовали экземпляр класса Product надо укозать binding = false
        @ModelAttribute(name = "product", binding = false) Product product,
        UpdateProductPayload payload,
        BindingResult bindingResult,
        Model model
    ) {
        try {
            this.productRestClient.updateProduct(product.id(), payload.title(), payload.details());
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch(BadRequestExeption exeption) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", exeption.getErrors());
            return "catalogue/products/edit";
        }
    }

    @PostMapping("delete")
    public String deleteProduct(
        @ModelAttribute("product") Product product
    ) {
        this.productRestClient.deleteProduct(product.id());
        return "redirect:/catalogue/products/list";
    }
}
