package ru.slava.catalogue.exeption;

import java.util.Locale;
import java.util.NoSuchElementException;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
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
}
