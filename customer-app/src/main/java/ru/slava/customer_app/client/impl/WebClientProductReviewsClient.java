package ru.slava.customer_app.client.impl;

import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.slava.customer_app.client.ProductReviewsClient;
import ru.slava.customer_app.entity.ProductReview;

public class WebClientProductReviewsClient implements ProductReviewsClient {

    private final WebClient webClient;

    public WebClientProductReviewsClient(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<ProductReview> findProductReviewsByProductId(int productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'findProductReviewsByProductId'");
    }

    @Override
    public Mono<ProductReview> createProductReview(int productId, int rating, String review) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createProductReview'");
    }

}
