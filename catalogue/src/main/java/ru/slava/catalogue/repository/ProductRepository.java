package ru.slava.catalogue.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import ru.slava.catalogue.entity.Product;


public interface ProductRepository extends CrudRepository<Product, Integer> {

    //find первое слова для запроса это найти
    //All второе слово все записи
    //By по полю Title
    //Like используется для поиска записей, где значение поля частично совпадает с переданным шаблоном.
    //SELECT * FROM product WHERE title LIKE '%Samsung%';
    //с использованием IgnoreCase мы игнорируем регистер
    Iterable<Product> findAllByTitleLikeIgnoreCase(String filter);
}
