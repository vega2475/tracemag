package edu.trace.feedbackservice.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewProductReviewPayload (
        @NotNull
        Integer productId,
        Integer rating,
        @Size(max = 1000)
        String review) {
}
