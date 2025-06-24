package ru.slava.customer_app.client;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.slava.customer_app.entity.ProductReview;

public interface ProductReviewsClient {
    Flux<ProductReview> findProductReviewsByProductId(int productId);

    Mono<ProductReview> createProductReview(int productId, int rating, String review);
}
