package ru.slava.feedback_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.slava.feedback_service.service.ProductReviewsService;

@RestController
@RequestMapping("feedback-api/product-reviews")
public class ProductReviewsRestController {

    private final ProductReviewsService productReviewsService;

    public ProductReviewsRestController(ProductReviewsService productReviewsService) {
        this.productReviewsService = productReviewsService;
    }

    
}
