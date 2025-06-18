package ru.slava.feedback_service.controller.payload;

public record NewProductReviewPayload(Integer productId, Integer rating, String reviews) {
}
