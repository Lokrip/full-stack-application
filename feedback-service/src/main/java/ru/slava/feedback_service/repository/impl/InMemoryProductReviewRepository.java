package ru.slava.feedback_service.repository.impl;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.slava.feedback_service.entity.ProductReview;
import ru.slava.feedback_service.repository.ProductReviewRepository;

@Repository
public class InMemoryProductReviewRepository implements ProductReviewRepository {

    private final List<ProductReview> productReviews = Collections.synchronizedList(
            new LinkedList<>());

    @Override
    public Mono<ProductReview> save(ProductReview productReview) {
        this.productReviews.add(productReview);
        // Метод Mono.just() в проекте Reactor (Project Reactor)
        // используется для создания реактивного объекта Mono, который содержит одно значение
        return Mono.just(productReview);
    }

    @Override
    public Flux<ProductReview> findAllByProductId(int productId) {
        return Flux.fromIterable(this.productReviews)
            .filter(productReview -> productReview.getProductId() == productId);
    }

}
