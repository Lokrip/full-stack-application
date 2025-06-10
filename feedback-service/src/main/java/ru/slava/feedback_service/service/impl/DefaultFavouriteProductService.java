package ru.slava.feedback_service.service.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.slava.feedback_service.entity.FavouriteProduct;
import ru.slava.feedback_service.repository.FavouriteProductRepository;
import ru.slava.feedback_service.service.FavouriteProductService;

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

    @Override
    public Mono<FavouriteProduct> findFavouriteProductByProduct(int productId) {
        return this.favouriteProductRepository.findByProductId(productId);
    }

    @Override
    public Flux<FavouriteProduct> findFavouriteProducts() {
        return this.favouriteProductRepository.findAll();
    }
}
