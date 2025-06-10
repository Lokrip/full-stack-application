package ru.slava.customer_app.controller;

import java.security.PublicKey;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;
import ru.slava.customer_app.client.ProductsClient;
import ru.slava.customer_app.controller.payload.NewProductReviewPayload;
import ru.slava.customer_app.entity.Product;
import ru.slava.customer_app.service.FavouriteProductService;
import ru.slava.customer_app.service.ProductReviewsService;

@Controller
@RequestMapping(value = "customer/products/{productId:\\d+}")
public class ProductController {

    private final ProductsClient productsClient;

    private final FavouriteProductService favouriteProductService;

    private final ProductReviewsService productReviewsService;

    public ProductController(ProductsClient productsClient,
            FavouriteProductService favouriteProductService,
            ProductReviewsService productReviewsService) {
        this.productReviewsService = productReviewsService;
        this.productsClient = productsClient;
        this.favouriteProductService = favouriteProductService;
    }

    @ModelAttribute(name = "product", binding = false)
    public Mono<Product> loadProduct(@PathVariable("productId") int id) {
        return this.productsClient.findProduct(id).switchIfEmpty(
                Mono.error(new NoSuchElementException("customer.products.error.not_found")));
    }

    @GetMapping
    public Mono<String> getProductPage(@PathVariable("productId") int id, Model model) {
        // если у нас mono stream пустой добавить переменнуж inFavourite за ранее
        model.addAttribute("inFavourite", false);
        return this.productReviewsService.findProductReviewsByProduct(id)
                .collectList()
                .doOnNext(productReviews -> model.addAttribute("reviews", productReviews))
                .then(this.favouriteProductService.findFavouriteProductByProduct(id)
                        // если нас mono stream не пустой то добавиться переменная inFavourite и
                        // перезапишется на true
                        .doOnNext(favouriteProduct -> model.addAttribute("inFavourite", true)))
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

    @PostMapping("remove-to-favourites")
    public Mono<String> removeProductFromFavorites(
            @ModelAttribute(name = "product", binding = false) Mono<Product> productMono) {
        return productMono
                .map(product -> product.id())
                // можно по разному подходить в формированию стрима
                .flatMap(productId -> this.favouriteProductService
                        .removeProductFromFavourites(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId)));
    }

    @PostMapping("create-review")
    public Mono<String> createReview(@PathVariable("productId") int id,
            @Valid @ModelAttribute NewProductReviewPayload payload,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("inFavourite", false);
            model.addAttribute("payload", payload);
            model.addAttribute("errors", bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .toList());
            return this.favouriteProductService.findFavouriteProductByProduct(id)
                    .doOnNext((favouriteProduct) -> model.addAttribute("inFavourite", true))
                    .thenReturn("customer/products/product");
        } else {
            return this.productReviewsService.createProductReview(id, payload.rating(), payload.review())
                    .thenReturn("redirect:/customer/products/%d".formatted(id));
        }
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException exception, Model model) {
        model.addAttribute("error", exception.getMessage());
        return "errors/404";
    }
}
