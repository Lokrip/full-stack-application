package ru.slava.catalogue.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import ru.slava.catalogue.entity.Product;

//Чтобы запустить тест с ограниченным окружениям
//каторый необходим иммено для data jpa
@DataJpaTest
@Sql("/sql/products.sql")
@Transactional
//так как у нас попыталась создаться
//база данных встраеваемая иза этого ошибка
//чтобы оона не создовалась надо укозать
//AutoConfigureTestDatabase
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryITests {

    @Autowired
    ProductRepository productRepository;

    @Test
    void findAllByTitleLikeIgnoreCase_ReturnsFilteredList() {
        // given
        var filter = "%шоколадка%";

        // then
        var products = this.productRepository.findAllByTitleLikeIgnoreCase(filter);

        // when
        assertEquals(List.of(new Product(2, "Шоколадка", "Очень вкусная шоколадка")), products);
    }
}
