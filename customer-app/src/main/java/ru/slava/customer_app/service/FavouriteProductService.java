package ru.slava.customer_app.service;

import reactor.core.publisher.Mono;
import ru.slava.customer_app.entity.FavouriteProduct;

public interface FavouriteProductService {
    Mono<FavouriteProduct> addProductToFavourites(int productId);

    Mono<Void> removeProductFromFavourites(int productId);

    Mono<FavouriteProduct> findFavouriteProductByProduct(int productId);
}
