package ru.slava.customer_app.client.impl;

import java.util.List;

import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.slava.customer_app.client.FavouriteProductsClient;
import ru.slava.customer_app.client.exception.ClientBadRequestException;
import ru.slava.customer_app.client.payload.NewFavouriteProductPayload;
import ru.slava.customer_app.entity.FavouriteProduct;

public class WebClientFavouriteProductsClient implements FavouriteProductsClient {

    private final WebClient webClient;

    public WebClientFavouriteProductsClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<FavouriteProduct> findFavouriteProducts() {
        return this.webClient
                .get()
                .uri("/feedback-api/favourite-products")
                .retrieve()
                .bodyToFlux(FavouriteProduct.class);
    }

    @Override
    public Mono<FavouriteProduct> findFavouriteProductByProductId(int productId) {
        return this.webClient
                .get()
                .uri("/feedback-api/favourite-products/by-product-id/{productId}", productId)
                .retrieve()
                .bodyToMono(FavouriteProduct.class)
                // даем завершиться стриму успешно даже если будет ошибка
                .onErrorComplete(WebClientResponseException.NotFound.class);
    }

    @Override
    public Mono<FavouriteProduct> addProductToFavourites(int productId) {
        return this.webClient
                .post()
                .uri("/feedback-api/favourite-products")
                .bodyValue(new NewFavouriteProductPayload(productId))
                .retrieve()
                .bodyToMono(FavouriteProduct.class)
                .onErrorMap(WebClientResponseException.BadRequest.class,
                        exception -> new ClientBadRequestException(exception,
                                ((List<String>) exception.getResponseBodyAs(ProblemDetail.class)
                                        .getProperties()
                                        .get("errors"))));
    }

    @Override
    public Mono<Void> removeProductFromFavourites(int productId) {
        return this.webClient
            .delete()
            .uri("/feedback-api/favourite-products/by-product-id/{productId}", productId)
            .retrieve()
            // переобразуем ответ в Mono<ResponseEntity<Void>>
            .toBodilessEntity()
            // и возврощаем его как Mono<Void>
            .then();
    }

}
