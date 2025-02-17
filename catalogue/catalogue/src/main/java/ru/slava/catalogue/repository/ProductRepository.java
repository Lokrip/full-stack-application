package ru.slava.catalogue.repository;

import java.util.List;
import java.util.Optional;

import ru.slava.catalogue.entity.Product;


public interface ProductRepository {

    List<Product> findAll();

    Product save(Product product);

    Optional<Product> findById(int productId);

    void deleteById(Integer id);

}
