package ru.slava.manager_app.controller.payload;

public record UpdateProductPayload(

    // @NotNull(message = "{catalogue.errors.product.update.errors.title_is_null}")
    // @Size(min = 3, max = 50, message = "{catalogue.errors.product.update.errors.title_size_is_invalid}")
    String title,

    // @Size(max = 550, message = "{catalogue.errors.product.update.errors.details_size_is_invalid}")
    String details) {}
