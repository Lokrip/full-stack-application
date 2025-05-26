package ru.slava.catalogue.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Locale;

//если нам нужно в итеграционных тестов поднимать староний сервисы каторый в докер или в операционноый машине нельзя поднять то нам нужен WireMock
//Для баз данных уже есть h2 и testcontainers
//а чтобы поднимать другие сервисы например какойта микросервис catalogue мы его можем за мокать тоесть имметировать этот сервис как реальнный нам нужен для этого WireMock

//в этой ситауций каждый метод тестовый будет выполняться в новой транзакций
//тоесть транзакция открываеться накатываються данные выполняеться метод
//и после проверки транзакция аткатываеться
@Transactional
@SpringBootTest
@AutoConfigureMockMvc
public class ProductsRestControllerITests {

    @Autowired
    MockMvc mockMvc;

    //нам надо наполнить базу данных какимето тестовыми данными чтобы тестить
    //можно это сделать создав экземпляр репозитория и потом наполнить даннми и после завешение удалить
    //но дучше использовать через Sql анатацию
    //это путь к resources и потом к файлу products.sql /sql/products.sql
    //данные каторый мы вносим для тестов они у нас остаються
    //после теста и нам желательно возврощать после теста
    //базу данных в исходное состояние это можно сделать через анатацию Transactional
    @Sql("/sql/products.sql")
    @Test
    void findProducts_ReturnsProductsList() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/catalogue-api/products")
            //этот запрос требуте jwt токен
            //builder мы можем дополнительно указать
            //парамметры нашего jwt каторый в дольнейшем
            //будет использован в spring security для
            //построенине объекта Auhentication
            .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        // when
        this.mockMvc.perform(requestBuilder)
        // then
            .andDo(print())
            .andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                content().json("""
                    [
                        {"id": 1, "title": "Товар №1", "details": "Описание товара №1"},
                        {"id": 2, "title": "Шоколадка", "details": "Очень вкусная шоколадка"},
                        {"id": 3, "title": "Товар №3", "details": "Описание товара №3"},
                        {"id": 4, "title": "Кефирка бутыла", "details": "Жирность 3,2%"}
                    ]""")
            );
    }

    @Sql("/sql/products.sql")
    @Test
    void findProducts_ReturnsProductsFiltersList() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/catalogue-api/products")
            //указываем парамметры запроса
            .param("filter", "товар")
            .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        // when
        this.mockMvc.perform(requestBuilder)
        // then
            .andDo(print())
            .andExpectAll(
                status().isOk(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                content().json("""
                    [
                        {"id": 1, "title": "Товар №1", "details": "Описание товара №1"},
                        {"id": 3, "title": "Товар №3", "details": "Описание товара №3"}
                    ]""")
            );
    }


    @Test
    void createProduct_RequestIsValid_ReturnsNewProduct() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/catalogue-api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                    {"title": "Ещё один новый товар", "details": "Какое-то описание нового товара"}""")
            .with(jwt().jwt(builder -> builder.claim("scope", "edit_catalogue")));

        //when
        this.mockMvc.perform(requestBuilder)
            //then
            .andDo(print())
            .andExpectAll(
                status().isCreated(),
                header().string(HttpHeaders.LOCATION, "http://localhost/catalogue-api/products/1"),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON),
                content().json("""
                        {
                            "id": 1,
                            "title": "Ещё один новый товар",
                            "details": "Какое-то описание нового товара"
                        }"""));
    }

    @Test
    void createProduct_RequestIsInvalid_ReturnsProblemDetail() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/catalogue-api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                    {"title": "  ", "details": null}""")
            //указываем локальзацию, по умолчанию будет использоваться системнная
            .locale(new Locale("ru", "RU"))
            .with(jwt().jwt(builder -> builder.claim("scope", "edit_catalogue")));

        //when
        this.mockMvc.perform(requestBuilder)
            //then
            .andDo(print())
            .andExpectAll(
                status().isBadRequest(),
                content().contentTypeCompatibleWith(MediaType.APPLICATION_PROBLEM_JSON),
                content().json("""
                        {
                            "errors": [
                                "Название товара должно быть от 3 до 50 символов"
                            ]
                        }""")); //если нам необходим строго проверить структуру все поля то мы можем укозать вторым аргументом true но в данном примере у нас не строгая проверка json мы можем проверить только одно поля json
    }


    @Test
    void createProduct_UserIsNotAuthorized_ReturnsForbidden() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.post("/catalogue-api/products")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                    {"title": "  ", "details": null}""")
            //указываем локальзацию, по умолчанию будет использоваться системнная
            .locale(new Locale("ru", "RU"))
            .with(jwt().jwt(builder -> builder.claim("scope", "view_catalogue")));

        //when
        this.mockMvc.perform(requestBuilder)
            //then
            .andDo(print())
            .andExpectAll(
                status().isForbidden());
    }
}
