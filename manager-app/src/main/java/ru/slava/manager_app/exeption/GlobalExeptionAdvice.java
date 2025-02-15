package ru.slava.manager_app.exeption;

import java.util.Locale;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletResponse;

@ControllerAdvice
public class GlobalExeptionAdvice {
    //MessageSource — это интерфейс в Spring Framework,
    //который используется для извлечения сообщений, обычно для интернационализации (i18n),
    //из ресурсов, таких как файлы свойств (properties files), и предоставления их приложениям.
    //Он позволяет приложениям
    //динамически выбирать сообщения, в зависимости от локали пользователя.
    private final MessageSource messageSource;

    @Autowired
    private GlobalExeptionAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementExeption(
        NoSuchElementException exception,
        Model model,
        HttpServletResponse response,
        Locale locale
    ) {
        //у ответа мы добовляем status 404 и когда будет ответ, он будет с этим статусом
        //Тоесть через HttpServletResponse мы можем менять ответ для пользователя
        response.setStatus(HttpStatus.NOT_FOUND.value());
        model.addAttribute("error",
            //первый аргумент мы принимаем сообщение кода ошибки
            //вторым мы передаем аргументы например у нас шаблонизированное сообщение и мы можем подстовлять аргументы
            //третий там мы указываем сообщение по умолчанию либо локаль но лучше писать сообщение по умолчанию на всякий случий если вдруг у нас в файле переводов отцутствует нужный перевод
            //четвертый там мы указываем локализацию
            this.messageSource.getMessage(
                exception.getMessage(),
                new Object[0],
                exception.getMessage(),
                locale
            ));
        return "errors/404";
    }
}
