package ru.slava.feedback_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import ru.slava.feedback_service.entity.FavouriteProduct;
import ru.slava.feedback_service.service.FavouriteProductService;

@RestController
@RequestMapping("feedback-api/favourite-products")
public class FavouriteProductsRestController {

    private final FavouriteProductService favouriteProductService;

    public FavouriteProductsRestController(FavouriteProductService favouriteProductService) {
        this.favouriteProductService = favouriteProductService;
    }

    // ❓ Почему нежелательно возвращать FavouriteProduct
    // напрямую из контроллера в REST/SOAP?
    // Класс FavouriteProduct — это сущность доменного уровня (Entity), т.е. она:
    // тесно связана с БД;
    // может содержать внутреннюю бизнес-логику или поля, которые
    // не должны попадать во внешний мир (например, id, userId, internalFlags);
    // может часто меняться из-за изменений в бизнес-логике или модели БД,
    // что приведёт к ломающим изменениям API.

    @GetMapping
    public Flux<FavouriteProduct> findFavouriteProducts() {
        return this.favouriteProductService.findFavouriteProducts();
    }
}
