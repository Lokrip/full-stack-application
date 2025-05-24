package ru.slava.customer_app.client.impl;

import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.slava.customer_app.client.ProductsClient;
import ru.slava.customer_app.entity.Product;

public class WebClientProductClient implements ProductsClient {

    private final WebClient webClient;

    public WebClientProductClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<Product> findAllProducts(String filter) {
        return this.webClient.get()
            .uri("/catalogue-api/products?filter={filter}", filter)
            //retrieve отпровляет запрос и возвращает ответ
            .retrieve()
            //мы можем переоброзовать тело ответа в flux
            .bodyToFlux(Product.class);
    }

    @Override
    public Mono<Product> findProduct(int id) {
        return this.webClient.get()
            .uri("/catalogue-api/products/{productId}", id)
            .retrieve()
            .bodyToMono(Product.class);
    }
}
