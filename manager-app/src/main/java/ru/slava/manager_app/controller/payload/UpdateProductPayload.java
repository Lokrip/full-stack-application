package ru.slava.manager_app.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateProductPayload(

    @NotNull
    @Size(min = 3, max = 50)
    String title,

    @Size(max = 550)
    String details) {}
