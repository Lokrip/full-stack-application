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

import reactor.core.publisher.Mono;
import ru.slava.customer_app.client.FavouriteProductsClient;
import ru.slava.customer_app.client.ProductReviewsClient;
import ru.slava.customer_app.client.ProductsClient;
import ru.slava.customer_app.client.exception.ClientBadRequestException;
import ru.slava.customer_app.controller.payload.NewProductReviewPayload;
import ru.slava.customer_app.entity.Product;

@Controller
@RequestMapping(value = "customer/products/{productId:\\d+}")
public class ProductController {

    private final ProductsClient productsClient;

    private final FavouriteProductsClient favouriteProductsClient;

    private final ProductReviewsClient productReviewsClient;

    public ProductController(ProductsClient productsClient,
            FavouriteProductsClient favouriteProductsClient,
            ProductReviewsClient productReviewsClient) {
        this.productsClient = productsClient;
        this.productReviewsClient = productReviewsClient;
        this.favouriteProductsClient = favouriteProductsClient;
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
        return this.productReviewsClient.findProductReviewsByProductId(id)
                .collectList()
                .doOnNext(productReviews -> model.addAttribute("reviews", productReviews))
                .then(this.favouriteProductsClient.findFavouriteProductByProductId(id)
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
                // В реактивном программировании на Java (например, с использованием Project
                // Reactor — Mono, Flux), оператор flatMap используется для асинхронного
                // преобразования элементов и «сплющивания» реактивных обёрток (Mono или Flux),
                // полученных в результате этого преобразования.
                // 🔍 Что делает flatMap?
                // Принимает каждый элемент исходного потока (Flux или Mono).
                // Применяет к нему функцию, которая возвращает новый Publisher (Mono или Flux).
                // Подписывается на каждый полученный Publisher.
                // Объединяет все их элементы в один результирующий поток.
                // flatMap принимает поток данных и преобразует каждый элемент
                // в новый поток (чаще всего Mono или Flux), а потом объединяет (сплющивает)
                // все эти внутренние потоки в один общий.
                // можно по разному подходить в формированию стрима
                .flatMap(productId -> this.favouriteProductsClient
                        .addProductToFavourites(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId)));
    }

    @PostMapping("remove-to-favourites")
    public Mono<String> removeProductFromFavorites(
            @ModelAttribute(name = "product", binding = false) Mono<Product> productMono) {
        return productMono
                .map(product -> product.id())
                // можно по разному подходить в формированию стрима
                .flatMap(productId -> this.favouriteProductsClient
                        .removeProductFromFavourites(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId)));
    }

    @PostMapping("create-review")
    public Mono<String> createReview(@PathVariable("productId") int id,
            @ModelAttribute NewProductReviewPayload payload,
            Model model) {
        return this.productReviewsClient.createProductReview(id, payload.rating(), payload.review())
                .thenReturn("redirect:/customer/products/%d".formatted(id))
                .onErrorResume(ClientBadRequestException.class, exception -> {
                    model.addAttribute("inFavourite", false);
                    model.addAttribute("payload", payload);
                    model.addAttribute("errors", exception.getErrors());
                    return this.favouriteProductsClient.findFavouriteProductByProductId(id)
                            .doOnNext((favouriteProduct) -> model.addAttribute("inFavourite", true))
                            .thenReturn("customer/products/product");
                });
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException exception, Model model) {
        model.addAttribute("error", exception.getMessage());
        return "errors/404";
    }
}
