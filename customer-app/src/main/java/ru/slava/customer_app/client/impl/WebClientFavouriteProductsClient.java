package ru.slava.customer_app.client.impl;


import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.slava.customer_app.client.FavouriteProductsClient;
import ru.slava.customer_app.entity.FavouriteProduct;

public class WebClientFavouriteProductsClient implements FavouriteProductsClient {

    private final WebClient webClient;

    public WebClientFavouriteProductsClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<FavouriteProduct> findFavouriteProducts() {
        return this.webClient.get()
            .uri("/feedback-api/favourite-products")
            .retrieve()
            .bodyToFlux(FavouriteProduct.class);
    }

    @Override
    public Mono<FavouriteProduct> findFavouriteProductByProductId(int productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findFavouriteProductByProductId'");
    }

    @Override
    public Mono<FavouriteProduct> addProductToFavourites(int productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addProductToFavourites'");
    }

    @Override
    public Mono<Void> removeProductFromFavourites(int productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeProductFromFavourites'");
    }

}
