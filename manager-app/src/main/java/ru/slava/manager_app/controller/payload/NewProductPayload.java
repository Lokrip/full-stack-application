package ru.slava.manager_app.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

//record это спецальный тип который предназначен для удобного создания неизменяемых (immutable) объектов с автоматической генерацией конструктора, геттеров, equals(), hashCode() и toString().
public record NewProductPayload(

    //поля title каторая нам переходит не должно быть null
    //и должна быть определенной длиной
    @NotNull
    @Size(min = 3, max = 50)
    String title,

    @Size(max = 550)
    String details) {
}
