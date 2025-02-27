package ru.slava.catalogue.exeption;

import java.util.Locale;
import java.util.NoSuchElementException;

import org.springframework.context.MessageSource;
import org.springframework.validation.BindException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class GlobalExeptionAdvice {

    private final MessageSource messageSource;

    public GlobalExeptionAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(
        NoSuchElementException exception,
        Locale locale
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ProblemDetail.forStatusAndDetail(
            HttpStatus.NOT_FOUND,
            this.messageSource.getMessage(
                exception.getMessage(),
                new Object[0],
                exception.getMessage(),
                locale
            )
        ));
    }


    @ExceptionHandler(BindException.class)
    public ResponseEntity<ProblemDetail> handleBindExeption(BindException exception, Locale locale) {
        ProblemDetail problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatus.BAD_REQUEST,
                        this.messageSource.getMessage(
                            "errors.400.title",
                            new Object[0],
                            "errors.40.0title",
                            locale
                        ));
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
