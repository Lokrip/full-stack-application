package ru.slava.customer_app.controller;

import javax.swing.Spring;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import reactor.core.publisher.Mono;
import ru.slava.customer_app.client.ProductsClient;
import ru.slava.customer_app.entity.FavouriteProduct;
import ru.slava.customer_app.service.FavouriteProductService;

@Controller
@RequestMapping(value = "customer/products")
public class ProductsController {

    private final ProductsClient productsClient;
    private final FavouriteProductService favouriteProductService;

    public ProductsController(
            ProductsClient productsClient,
            FavouriteProductService favouriteProductService) {
        this.favouriteProductService = favouriteProductService;
        this.productsClient = productsClient;
    }

    @GetMapping("/list")
    // Mono — это часть реактивной библиотеки Project Reactor и представляет собой
    // тип из
    // реактивного программирования, предназначенный для асинхронной обработки
    // одного значения или ошибки.
    // Он используется, в частности, в Spring WebFlux — это реактивный
    // web-фреймворк,
    // альтернатива классическому Spring MVC.
    // Mono<T> — реактивный тип, который либо содержит одно значение типа T, либо
    // завершён с ошибкой, либо пустой (без значения).
    // Это «одноразовый» поток (reactive stream), который испускает (emits) максимум
    // одно значение.
    // Mono<T> максимум одно значение (0 или 1)
    // Flux<T> поток значений (0..n)
    public Mono<String> getProductsListPage(Model model,
            @RequestParam(name = "filter", required = false) String filter) {
        model.addAttribute("filter", filter);
        return this.productsClient.findAllProducts(filter)
                // collectList переобразуем его в Mono список Mono<List<Product>>
                .collectList()
                // добовляем для текущего элемента stream обрабодчик
                // В вашем коде doOnNext используется в реактивном стриме
                // (Reactor Project), и он выполняет побочное действие
                // (side-effect) для каждого элемента, который проходит по стриму на данном
                // этапе.
                // Метод doOnNext позволяет вам выполнить какой-либо побочный эффект
                // (например, логирование, обновление состояния, отладку),
                // не изменяя сам поток данных. Он не влияет на данные
                // и не прерывает цепочку операторов.
                .doOnNext(products -> model.addAttribute("products", products))
                .thenReturn("customer/products/list");
    }

    @GetMapping("favourites")
    public Mono<String> getFavouriteProductsPage(Model model,
            @RequestParam(name = "filter", required = false) String filter) {
        model.addAttribute("filter", filter);
        return this.favouriteProductService.findFavouriteProducts()
                .map(FavouriteProduct::getProductid)
                .collectList()
                .flatMap(favouriteProducts -> this.productsClient.findAllProducts(filter)
                        .filter(product -> favouriteProducts.contains(product.id()))
                        .collectList()
                        .doOnNext(products -> model.addAttribute("products", products)))
                .thenReturn("customer/products/favourites");
    }

}
