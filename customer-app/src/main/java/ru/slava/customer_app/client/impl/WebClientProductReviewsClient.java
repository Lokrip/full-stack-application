package ru.slava.customer_app.client.impl;

import java.util.List;

import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.slava.customer_app.client.ProductReviewsClient;
import ru.slava.customer_app.client.exception.ClientBadRequestException;
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
                // мы переобразуем при помощи данного метода
                // текущую ошибку каторая содержиться в нашем реактивном stream
                // на ту каторый мы в метод передадим, 1 аргументом мы передаем exception
                // каторый хотим поменять а втором аргмент мы передаем exception каторый мы
                // поменяем на обрабодку вместо первого чтобы не первый срабатывал а второй
                .onErrorMap(WebClientResponseException.BadRequest.class,
                        exception -> new ClientBadRequestException(exception,
                                ((List<String>) exception.getResponseBodyAs(ProblemDetail.class)
                                        .getProperties()
                                        .get("errors"))));
    }

}
