package ru.slava.customer_app.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
import ru.slava.customer_app.entity.FavouriteProduct;
import ru.slava.customer_app.repository.FavouriteProductRepository;
import ru.slava.customer_app.service.FavouriteProductService;

@Service
public class DefaultFavouriteProductService implements FavouriteProductService {

    private final FavouriteProductRepository favouriteProductRepository;

    public DefaultFavouriteProductService(FavouriteProductRepository favouriteProductRepository) {
        this.favouriteProductRepository = favouriteProductRepository;
    }

    @Override
    public Mono<FavouriteProduct> addProductToFavourites(int productId) {
        return this.favouriteProductRepository.save(new FavouriteProduct(UUID.randomUUID(), productId));
    }

    @Override
    public Mono<Void> removeProductFromFavourites(int productId) {
        return this.favouriteProductRepository.deleteByProductId(productId);
    }

}
