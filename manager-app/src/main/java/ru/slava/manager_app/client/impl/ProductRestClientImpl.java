package ru.slava.manager_app.client.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import ru.slava.manager_app.client.ProductRestClient;
import ru.slava.manager_app.controller.payload.NewProductPayload;
import ru.slava.manager_app.controller.payload.UpdateProductPayload;
import ru.slava.manager_app.entity.Product;
import ru.slava.manager_app.exeption.BadRequestExeption;

public class ProductRestClientImpl implements ProductRestClient {
    //Это ножно чтобы jakson библеотека каторый парсит json объекты и json строки
    //переобразует их java объекты могла понять что у нас список товаров
    private static final ParameterizedTypeReference<List<Product>> PRODUCTS_TYPE_REFERENCE =
        new ParameterizedTypeReference<>() {};
    private final RestClient restClient;

    @Autowired
    public ProductRestClientImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<Product> findAllProducts() {
        return this.restClient
            //Это метод запроса
            .get()
            //это uri адресс запроса
            .uri("/catalogue-api/products")
            //retrieve он нужен чтобы получить ответ
            .retrieve()
            //body он нужен чтобы переобразовывать тело ответа к какомуто виду
            .body(PRODUCTS_TYPE_REFERENCE);

    }

    @Override
    public Product createProduct(String title, String details) {
        try {
            return this.restClient
                .post()
                .uri("/catalogue-api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new NewProductPayload(title, details))
                .retrieve()
                .body(Product.class);
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            if(problemDetail.getProperties().isEmpty()) {
                throw new RuntimeException("Not found Properties");
            }

            if(!(problemDetail.getProperties().get("errors") instanceof List<?>)) {
                throw new RuntimeException("Not found errors");
            }

            throw new BadRequestExeption((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public Optional<Product> findProduct(int productId) {
        try {
            return Optional.ofNullable(this.restClient
                .get()
                .uri("/catalogue-api/products/{productId}", productId)
                .retrieve()
                .body(Product.class));
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }

    @Override
    public void updateProduct(int productId, String title, String details) {
        try {
            this.restClient
                .patch()
                .uri("/catalogue-api/products/{productId}", productId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new UpdateProductPayload(title, details))
                .retrieve()
                //Метод toBodilessEntity() в вашем коде используется для того, чтобы получить ответ от сервера без тела (body), то есть, только с HTTP статусом и заголовками, но без содержимого.
                .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            if(problemDetail.getProperties().isEmpty()) {
                throw new RuntimeException("Not found Properties");
            }

            if(!(problemDetail.getProperties().get("errors") instanceof List<?>)) {
                throw new RuntimeException("Not found errors");
            }

            throw new BadRequestExeption((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void deleteProduct(int productId) {
        try {
            Optional.ofNullable(this.restClient
                .delete()
                .uri("/catalogue-api/products/{productId}", productId)
                .retrieve()
                .toBodilessEntity());
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NoSuchElementException(exception);
        }
    }

}
