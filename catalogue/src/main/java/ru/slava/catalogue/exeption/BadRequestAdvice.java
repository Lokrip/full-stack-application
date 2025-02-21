package ru.slava.catalogue.exeption;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BadRequestAdvice {

    private final MessageSource messageSource;

    public BadRequestAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    //BindException тоесть мы перехватываем ошибку валидаций BindException и обрабатываем ее
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindExeption(BindException exception, Locale locale) {
        //ProblemDetail — это структура, которая используется в веб-разработке для представления
        //информации о проблемах или ошибках, которые могут возникать при обработке HTTP-запросов.
        //Она является частью спецификации RFC 7807 и предоставляет стандартный способ для API и
        //серверов сообщать о возникших ошибках в формате JSON.
        //1. forStatusAndDetail(HttpStatus status, String detail)
        //Этот метод создает ProblemDetail с заданным статусом HTTP и подробным сообщением об ошибке.
        //2. forStatus(HttpStatus status)
        //Этот метод создает ProblemDetail с заданным статусом HTTP, но без подробного описания ошибки. Обычно в таких случаях можно использовать только статус и стандартное сообщение.
        //3. forTypeAndStatus(String type, HttpStatus status)
        //Этот метод создает ProblemDetail с заданным типом ошибки и HTTP статусом.
        //4. forStatusAndType(HttpStatus status, String type)
        //Этот метод похож на предыдущий, но порядок параметров может различаться. Он создает ProblemDetail с заданным статусом и типом ошибки.
        //5. forStatusAndDetail(HttpStatus status, String title, String detail)
        //В некоторых случаях может быть метод, который позволяет задать и title, и detail вместе с status.
        //6. setProperty(String key, Object value)
        //Этот метод позволяет добавить дополнительные свойства в объект ProblemDetail. Это расширение для хранения дополнительных данных, которые могут быть полезны в контексте ошибки.
        ProblemDetail problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatus.BAD_REQUEST,
                        this.messageSource.getMessage(
                            "errors.400.title",
                            new Object[0],
                            "errors.40.0title",
                            locale
                        ));

        //Метод ссылки :: является краткой формой записи для выражения лямбда-функции, например, вместо:
        //.map(error -> error.getDefaultMessage())
        //используем .map(ObjectError::getDefaultMessage)
        problemDetail.setProperty("errors",
            exception.getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .toList());

        return ResponseEntity
            .badRequest()
            .body(problemDetail);
    }
}
