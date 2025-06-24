package ru.slava.feedback_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.slava.feedback_service.controller.payload.NewProductReviewPayload;
import ru.slava.feedback_service.entity.ProductReview;
import ru.slava.feedback_service.service.ProductReviewsService;

@RestController
@RequestMapping("feedback-api/product-reviews")
public class ProductReviewsRestController {

    private final ProductReviewsService productReviewsService;

    public ProductReviewsRestController(ProductReviewsService productReviewsService) {
        this.productReviewsService = productReviewsService;
    }

    @GetMapping("by-product-id/{productId:\\d+}")
    public Flux<ProductReview> findProductReviewsByProductId(@PathVariable("productId") int productId) {
        return this.productReviewsService.findProductReviewsByProduct(productId);
    }

    // если NewProductReviewPayload не валидин то в случий не реактивнных типов
    // мы используем BindingResult вторым аргументом но если NewProductReviewPayload
    // указон в Mono или Flux нам надо обработать ответ от валидаций другим образом
    // Тоесть если NewProductReviewPayload не валидин то Mono станоновиться
    // ошибочнным,
    // и внутри его возникает исключение каторая называеться WebExchaneBindException
    @PostMapping
    public Mono<ResponseEntity<ProductReview>> createProductReview(
            @Valid @RequestBody Mono<NewProductReviewPayload> payloadMono,
            UriComponentsBuilder uriComponentsBuilder) {

        // flatMap был создан, чтобы асинхронно модифицировать (обрабатывать)
        // данные, полученные из потока, и объединить все результаты в один поток.
        return payloadMono
                .flatMap(payload -> this.productReviewsService
                        .createProductReview(payload.productId(), payload.rating(), payload.reviews()))
                .map(productReview -> ResponseEntity
                        .created(uriComponentsBuilder
                                .replacePath("/feedback-api/product-reviews/{id}")
                                .build("id", productReview.getId()))
                        .body(productReview));
    }
}
