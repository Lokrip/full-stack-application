package ru.slava.customer_app.client.impl;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

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
            .bodyToMono(Product.class)
            // мы можем добавить обрабодку ошибок например Для WebClientResponseException
            // эта ошибка вызываеться когда запись по id или другом название нету
            // onErrorComplete он успешно завершает stream при наличий определнного типа ошибки
            // тоесть вместо ошибки WebClientResponseException
            // мы возврощаем нормальный stream только он будет пустой
            .onErrorComplete(WebClientResponseException.NotFound.class);
    }
}
