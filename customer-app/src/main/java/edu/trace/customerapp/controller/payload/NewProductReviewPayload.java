package edu.trace.customerapp.controller.payload;

public record NewProductReviewPayload (
        Integer rating,
        String review
){
}
