package edu.trace.customerapp.client.payload;

public record NewProductReviewPayload(Integer productId, Integer rating, String review) {
}
