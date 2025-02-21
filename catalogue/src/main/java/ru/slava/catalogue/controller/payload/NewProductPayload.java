package ru.slava.catalogue.controller.payload;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

//record это спецальный тип который предназначен для удобного создания неизменяемых (immutable) объектов с автоматической генерацией конструктора, геттеров, equals(), hashCode() и toString().
public record NewProductPayload(

    //поля title каторая нам переходит не должно быть null
    //и должна быть определенной длиной
    @NotNull(message = "{catalogue.errors.product.create.errors.title_is_null}")
    //message это содержимое ошибки когда запись не прошла валидаций
    //catalogue.errors.product.create.errors.title_is_invalid
    //это ключь в messages.properties мы его берем там
    //мы можем использовать {} для message это нужно чтобы сообщение автоматический перевилось и мы не будем
    //использовать MessageSource для перевода сообщение ошибки
    @Size(min = 3, max = 50, message = "{catalogue.errors.product.create.errors.title_size_is_invalid}")
    String title,

    @Size(max = 550, message = "{catalogue.errors.product.create.errors.details_size_is_invalid}")
    String details) {
}
