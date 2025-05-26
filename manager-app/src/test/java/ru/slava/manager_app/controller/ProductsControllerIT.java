package ru.slava.manager_app.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester.MockMvcRequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;

import ru.slava.manager_app.entity.Product;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

//интеграционный тест.

//@SpringBootTest он нужен для того чтобы запустить context приложения
//в тестовом режиме для того чтобы можно было оброщаться к нашему приложению
@SpringBootTest
//Аннотация @AutoConfigureMockMvc в Spring Boot используется для автоматической
//настройки объекта MockMvc, который
//применяется для тестирования контроллеров (и других веб-слоев) без запуска самого сервера.
@AutoConfigureMockMvc
//(
    //printOnlyOnFailure = false он делает так что буду логироваться все запросы
    // printOnlyOnFailure = false
// )
//Чтобы за мокать или имметировать сервис нам нужен WireMockTest
//Так как это отдельнная тестовая среда мы ее запускаем на порту 54321
@WireMockTest(httpPort = 54321)
public class ProductsControllerIT {

    // MockMvc в Spring Boot — это инструмент для тестирования
    // веб-контроллеров без необходимости запускать полноценный
    // веб-сервер (например, Tomcat). Он позволяет выполнять
    // HTTP-запросы (GET, POST и др.) к контроллерам внутри
    // тестов и проверять результаты (статус, тело ответа, заголовки и т.д.).
    //внедряем экземпляр MockMvc
    @Autowired
    public MockMvc mockMvc;

    @Test
    void getProductsList_ReturnsProductsListPage() throws Exception {
        // given
        var requestBuilder = MockMvcRequestBuilders.get("/catalogue/products/list")
            .queryParam("filter", "товар")
            .with(user("j.dewar").roles("MANAGER"));


        //WireMock он нужен если у нас нету возможностей развернуть сервер catalogue где api
        //но нам нужно протестить manager_app WireMock с имметирует mock сервер и потом можно уже на него
        //отпровлять запрос

        //иметируем мок сервер
        //мы буем оставлять има класса в катором эти статические методы
        //просто если мы просто импортировали методы то
        //одни методы можно спутать с другими методами из тавоже mokita
        WireMock.stubFor(WireMock.get(WireMock.urlPathMatching("/catalogue-api/products"))
            .withQueryParam("filter", WireMock.equalTo("товар"))
            .willReturn(WireMock.ok("""
                [
                    {"id": 1, "title": "Товар №1", "details": "Описание товара №1"},
                    {"id": 2, "title": "Товар №2", "details": "Описание товара №2"}
                ]""").withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)));

        //then
        this.mockMvc.perform(requestBuilder)
            //then
            .andDo(print())
            //andExpectAll он используется для тестирования ответа от запроса.
            .andExpectAll(
                status().isOk(),
                view().name("catalogue/products/list"),
                model().attribute("filter", "товар"),
                model().attribute("products", List.of(
                    new Product(1, "Товар №1", "Описание товара №1"),
                    new Product(2, "Товар №2", "Описание товара №2")
                ))
            );
        //проверяем был ли произведен запрос на этот uri с оперделнными параметрами запроса
        WireMock.verify(WireMock.getRequestedFor(WireMock.urlPathMatching("/catalogue-api/products"))
            .withQueryParam("filter", WireMock.equalTo("товар")));
    }

    @Test
    // @WithMockUser(
    //     value = "j.dewar",
    //     roles = "MANAGER"
    // )
    public void getNewProductPage_ReturnsProductPage() throws Exception {
        // given

        //данный запрос будет выполняться от иммени пользователя
        //j.dewar с ролью MANAGER
        //такого же эффекта можно добиться через через анатацию
        //WithMockUser
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/catalogue/products/create")
            // метод .with(...) добавляет в запрос нужные данные, в данном случае
            // аутентификационные данные пользователя, включая заголовки авторизации
            // (Authorization header) и другие элементы, которые имитируют
            // вход пользователя в систему. Это своего рода "внедрение"
            // пользователя в запрос при помощи SecurityMockMvcRequestPostProcessors.
            .with(user("j.dewar").roles("MANAGER"));

        // when
        //perform выполяет запрос на сервер
        this.mockMvc.perform(requestBuilder)
        // then
            //andDo через него мы можем получить просто доступ
            //к ответу и выполнить какиета манипуляций

            //print он нужет чтобы вывести ответ от запроса
            //Можно ввыводить информацию о запросах
            //и ответах в AutoConfigureMockMvc
            .andDo(print())
            //валидируем наш ответа от запрос
            //после логга ответа
            //это нужно если при валидаций
            //будет ошибка то мы увидим лог а потом будет ошибка
            .andExpectAll(
                //если status ок то ответ проходит валидацию
                status().isOk(),
                view().name("catalogue/products/new_product")
            );
    }
}
