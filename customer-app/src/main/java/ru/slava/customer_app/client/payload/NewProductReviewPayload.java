package ru.slava.customer_app.client.payload;

public record NewProductReviewPayload(int productId, int rating, String review) {
}
