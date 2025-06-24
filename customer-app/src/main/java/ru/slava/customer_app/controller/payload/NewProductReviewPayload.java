package ru.slava.customer_app.controller.payload;

public record NewProductReviewPayload(
        Integer rating,
        String review) {
}
