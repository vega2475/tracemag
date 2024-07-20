package edu.trace.feedbackservice.controller.payload;

import jakarta.validation.constraints.NotNull;

public record NewFavouriteProductPayload (@NotNull Integer productId){
}
