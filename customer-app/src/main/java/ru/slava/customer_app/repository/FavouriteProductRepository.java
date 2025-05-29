package ru.slava.customer_app.repository;

import reactor.core.publisher.Mono;
import ru.slava.customer_app.entity.FavouriteProduct;

public interface FavouriteProductRepository {

    Mono<FavouriteProduct> save(FavouriteProduct favouriteProduct);

    Mono<Void> deleteByProductId(int productId);

    Mono<FavouriteProduct> findByProductId(int productId);
}
