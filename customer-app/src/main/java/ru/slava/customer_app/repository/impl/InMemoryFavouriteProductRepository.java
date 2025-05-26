package ru.slava.customer_app.repository.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;
import ru.slava.customer_app.entity.FavouriteProduct;
import ru.slava.customer_app.repository.FavouriteProductRepository;

@Repository
public class InMemoryFavouriteProductRepository implements FavouriteProductRepository {

    private final List<FavouriteProduct> favouriteProducts = Collections
            .synchronizedList(new LinkedList<>());

    @Override
    public Mono<FavouriteProduct> save(FavouriteProduct favouriteProduct) {
        this.favouriteProducts.add(favouriteProduct);
        return Mono.just(favouriteProduct);
    }

    @Override
    public Mono<Void> deleteByProductId(int productId) {
        this.favouriteProducts.removeIf(
            favouriteProduct -> favouriteProduct.getProductid() == productId);
        return Mono.empty();
    }

}
