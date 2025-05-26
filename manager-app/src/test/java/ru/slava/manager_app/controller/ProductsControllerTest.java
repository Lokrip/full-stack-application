package ru.slava.manager_app.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.ConcurrentModel;

import ru.slava.manager_app.client.ProductRestClient;
import ru.slava.manager_app.controller.payload.NewProductPayload;
import ru.slava.manager_app.entity.Product;
import ru.slava.manager_app.exeption.BadRequestExeption;

//unit tests - модульнные тесты

//Аннотация @ExtendWith(MockitoExtension.class) в
//JUnit 5 используется для интеграции с Mockito, который
//является популярной библиотекой для создания моков в тестах
//Эта аннотация позволяет автоматически инициализировать моки, которые помечены
//аннотациями @Mock в тестовом классе. Она также предоставляет
//поддержку для создания моков и других возможностей, таких как
//настройка поведения моков, в тестах с использованием JUnit 5.
@ExtendWith(MockitoExtension.class)
@DisplayName("Модульные тесты ProductsController")
public class ProductsControllerTest {

    //Мок (или mock) — это объект, который используется в
    //тестировании для имитации поведения реального объекта.
    //Моки помогают изолировать тестируемый код от зависимости,
    //позволяя протестировать его логику без необходимости обращаться к
    //реальным внешним компонентам, таким как базы данных, API, сетевые сервисы или другие объекты.

    //создаем мок объект каторый имметирует что это класс ProductRestClient
    // public ProductRestClient productRestClient = Mockito.mock(ProductRestClient.class);
    //создаем мок объект каторый имметирует что это класс ProductRestClient

    //ProductRestClient он хоть и обявлен но методы его не чего не возврощаеют
    //нам необходимо сымитировать поведения даного мок объекта
    @Mock
    public ProductRestClient productRestClient; //тоже самое как public ProductRestClient productRestClient = Mockito.mock(ProductRestClient.class);

    // public ProductController controller = new ProductController(this.productRestClient);

    // InjectMocks он сам определает что нужно создать экземляр класса ProductController
    // и передать в него мок объеты например ProductRestClient в качестве аргумента конструктора
    @InjectMocks
    public ProductsController controller; //это тоже самое как public ProductController controller = new ProductController(this.productRestClient);


    //можно еще иницилизировать данные об ProductController
    //в методах каторый будут выполняться перед каждым тестом
    //такие методы в junit 5 омечаються анатацией BeforeEach или BeforeAll
    //BeforeEach методы отмеченной этой онатацией вызываються перед каждым тестовым
    //методом
    //BeforeAll методы отмеченной этой онатацией вызываеться перед всеми тестовыми методами
    //для того чтобы пере инцилизировать объект каторый будет доступен все тестовым методам


    //первое название метода это название тестируемого метода
    //вторая часть это условия при катором тестируеться метод
    //третея часть это ожидаемый результат теста
    @Test
    //DisplayName он нужен чтобы описать что проиходит в данном тесте
    //он будет выводиться в логгах и тд описывая тест
    //его можно использовать на уровне класса
    @DisplayName("createProduct создаст новый товар и перенаправит на страницу товара")
    public void createProduct_RequestIsValid_ReturnsRedirectionToProductPage() {
        //тест обычно состоит из 3 частей определенние состояни при котором
        //будет протикать тест из вызова тестируемого метода и
        //с проверки результата

        //AAA такая структура так называеться Arrange-Act-Assert (AAA)
        //Arrange (Подготовка / given)
        //Настройка тестовой среды: создание объектов, настройка моков, определение входных данных.
        //Act (Действие / when)
        //Выполнение тестируемого действия: вызов метода, выполнение запроса и т.д.
        //Assert (Проверка / then)
        //Проверка результата: убедиться, что поведение/результат соответствует ожидаемому.

        //given
        var payload = new NewProductPayload("Новый товар", "Описания нового товара");
        var model = new ConcurrentModel();

        //doReturn(значение) — говорит Mockito: "Когда вызовут метод, верни это значение".
        doReturn(new Product(
            1,
            "Новый товар",
            "Описание нового товара")
        )
        //when(объект) — указывает, для какого объекта действует правило.
            .when(this.productRestClient)
        //указывает, какой именно метод и с какими аргументами должен быть вызван.
            .createProduct("Новый товар", "Описания нового товара");
        //указывает что данные передоваться будут любые но не пустые
            // .createProduct(notNull(), any());



        //when
        var result = this.controller.createProduct(payload, model);


        //then
        assertEquals("redirect:/catalogue/products/1", result);

        //можем проверить что данные productRestClient.createProduct метод был действительно вызван
        //через verfiy
        verify(this.productRestClient).createProduct("Новый товар", "Описания нового товара");
        //можем проверить что к данному мок объекту productRestClient не было больше обращений
        verifyNoMoreInteractions(this.productRestClient);
    }


    @Test
    @DisplayName("createProduct вернет страницу с ошибками, если запрос невалиден")
    public void createProduct_RequestIsInValid_ReturnsProductFormWithErrors() {
        //given
        var payload = new NewProductPayload("   ", null);
        var model = new ConcurrentModel();
        var errors = List.of("Ошибка 1", "Ошибка 2");

        //doThrow Он подготавливает заглушку (stub)
        //сообщает Mockito, что если в процессе выполнения
        //теста будет вызван this.productRestClient.createProduct(" ", null),
        //то должен быть выброшен BadRequestExeption.
        //doThrow(исключения) — говорит Mockito: "Когда вызовут метод, верни это исключения".
        doThrow(new BadRequestExeption(errors))
            .when(this.productRestClient)
            .createProduct("   ", null);



        //when
        var result = this.controller.createProduct(payload, model);


        //then
        assertEquals("catalogue/products/new_product", result);
        assertEquals(payload, model.getAttribute("payload"));
        assertEquals(errors, model.getAttribute("errors"));

        verify(this.productRestClient).createProduct("   ", null);
        verifyNoMoreInteractions(this.productRestClient);
    }
}


//Mockito — это библиотека для Java, которая
//позволяет имитировать (или подменять) поведение объектов в тестах.
//Это называется мокация (mocking).
