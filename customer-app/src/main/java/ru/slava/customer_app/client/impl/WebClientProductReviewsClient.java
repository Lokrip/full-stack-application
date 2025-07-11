package ru.slava.customer_app.client.impl;

import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.slava.customer_app.client.ProductReviewsClient;
import ru.slava.customer_app.client.payload.NewProductReviewPayload;
import ru.slava.customer_app.entity.ProductReview;

public class WebClientProductReviewsClient implements ProductReviewsClient {

    private final WebClient webClient;

    public WebClientProductReviewsClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<ProductReview> findProductReviewsByProductId(int productId) {
        return this.webClient
                .get()
                .uri("/feedback-api/product-reviews/by-product-id/{productId}", productId)
                .retrieve()
                .bodyToFlux(ProductReview.class);

    }

    @Override
    public Mono<ProductReview> createProductReview(int productId, int rating, String review) {
        return this.webClient
                .post()
                .uri("/feedback-api/product-reviews")
                .bodyValue(new NewProductReviewPayload(productId, rating, review))
                .retrieve()
                .bodyToMono(ProductReview.class)
                .onErrorMap(null);
    }

}
