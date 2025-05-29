package ru.slava.customer_app.controller;

import java.security.PublicKey;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import reactor.core.publisher.Mono;
import ru.slava.customer_app.client.ProductsClient;
import ru.slava.customer_app.entity.Product;
import ru.slava.customer_app.service.FavouriteProductService;

@Controller
@RequestMapping(value = "customer/products/{productId:\\d+}")
public class ProductController {

    private final ProductsClient productsClient;

    private final FavouriteProductService favouriteProductService;

    public ProductController(ProductsClient productsClient,
            FavouriteProductService favouriteProductService) {
        this.productsClient = productsClient;
        this.favouriteProductService = favouriteProductService;
    }

    @ModelAttribute(name = "product", binding = false)
    public Mono<Product> loadProduct(@PathVariable("productId") int id) {
        return this.productsClient.findProduct(id);
    }

    @GetMapping
    public Mono<String> getProductPage(@PathVariable("productId") int id, Model model) {
        return this.favouriteProductService.findFavouriteProductByProduct(id)
            .doOnNext(favouriteProduct -> model.addAttribute("inFavourite", true))
            .thenReturn("customer/products/product");
    }

    @PostMapping("add-to-favourites")
    public Mono<String> addProductToFavorites(
            @ModelAttribute(name = "product", binding = false) Mono<Product> productMono) {
        return productMono
                .map(product -> product.id())
                // можно по разному подходить в формированию стрима
                .flatMap(productId -> this.favouriteProductService
                        .addProductToFavourites(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId)));
    }

    @PostMapping("delete-to-favourites")
    public Mono<String> deleteProductFromFavorites(
            @ModelAttribute(name = "product", binding = false) Mono<Product> productMono) {
        return productMono
                .map(product -> product.id())
                // можно по разному подходить в формированию стрима
                .flatMap(productId -> this.favouriteProductService
                        .removeProductFromFavourites(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId)));
    }
}
