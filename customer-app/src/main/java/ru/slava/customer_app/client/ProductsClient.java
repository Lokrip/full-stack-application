package ru.slava.customer_app.client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.slava.customer_app.entity.Product;

public interface ProductsClient {

    // Flux — это реактивный тип из библиотеки Project Reactor, который представляет
    // собой асинхронный поток данных, содержащий 0 или более элементов.
    // Он используется в Spring WebFlux и других реактивных
    // приложениях для обработки последовательностей значений во времени.
    Flux<Product> findAllProducts(String filter);

    Mono<Product> findProduct(int id);
}
