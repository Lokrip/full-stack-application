package ru.slava.catalogue.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import ru.slava.catalogue.entity.Product;


public interface ProductRepository extends CrudRepository<Product, Integer> {
    //можно писать свой запросы
    //тут мы указываем свойста класса Product а не название в таблиц и его полей в миграций
    @Query(value = "select p from Product p where p.title ilike :filter")
    //@Param("filter") делает так что аргумент filter становитьься прамаетром запроса в :filter
    Iterable<Product> getAllProductByTitle(@Param("filter") String filter);
    //find первое слова для запроса это найти
    //All второе слово все записи
    //By по полю Title
    //Like используется для поиска записей, где значение поля частично совпадает с переданным шаблоном.
    //SELECT * FROM product WHERE title LIKE '%Samsung%';
    //с использованием IgnoreCase мы игнорируем регистер
    Iterable<Product> findAllByTitleLikeIgnoreCase(String filter);


    //можно писать свой нативыне sql запросы если укозать nativeQuery = true
    @Query(value = "select * from catalogue.t_product where c_title ilike :filter", nativeQuery = true)
    Iterable<Product> sqlGetAllProductByTitle(@Param("filter") String filter);

    //можно использовать свой имменованые sql запросы
    @Query(name = "Product.findAllByTitleLikeIgnoringCase", nativeQuery = true)
    Iterable<Product> namedSqlGetAllProductByTitle(@Param("filter") String filter);
}
