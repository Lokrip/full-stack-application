package ru.slava.feedback_service.repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.slava.feedback_service.entity.ProductReview;

public interface ProductReviewRepository {
    Mono<ProductReview> save(ProductReview productReview);

    Flux<ProductReview> findAllByProductId(int productId);
}
