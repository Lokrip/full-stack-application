package ru.slava.manager_app.controller.payload;

//record это спецальный тип который предназначен для удобного создания неизменяемых (immutable) объектов с автоматической генерацией конструктора, геттеров, equals(), hashCode() и toString().
public record NewProductPayload(String title, String details) {
} 
