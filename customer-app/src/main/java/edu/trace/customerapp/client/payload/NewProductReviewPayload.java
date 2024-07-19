package edu.trace.customerapp.client.payload;

import jakarta.validation.constraints.NotNull;

public record NewProductReviewPayload(
        @NotNull Integer rating,
        String review) {
}
