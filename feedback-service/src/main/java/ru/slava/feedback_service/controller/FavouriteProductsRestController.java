package ru.slava.feedback_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.validation.Valid;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.slava.feedback_service.controller.payload.NewFavouriteProductPayload;
import ru.slava.feedback_service.entity.FavouriteProduct;
import ru.slava.feedback_service.service.FavouriteProductService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @GetMapping("by-product-id/{productId:\\d+}")
    public Mono<FavouriteProduct> findFavouriteProductByProductId(@PathVariable("productId") int productId) {
        return this.favouriteProductService.findFavouriteProductByProduct(productId);
    }

    @PostMapping
    public Mono<ResponseEntity<FavouriteProduct>> addProductToFavourites(
            @Valid @RequestBody Mono<NewFavouriteProductPayload> payloadMono,
            UriComponentsBuilder uriComponentsBuilder) {
        return payloadMono
                .flatMap(payload -> this.favouriteProductService.addProductToFavourites(payload.productId()))
                .map(favouriteProducct -> ResponseEntity
                        .created(uriComponentsBuilder.replacePath("feedback-api/favourite-products/{id}")
                                .build(favouriteProducct.getId()))
                        .body(favouriteProducct));
    }

    @DeleteMapping("by-product-id/{productId:\\d+}")
    public Mono<ResponseEntity<Void>> removeProductFromFavourites(@PathVariable("productId") int productId) {
        return this.favouriteProductService.removeProductFromFavourites(productId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
