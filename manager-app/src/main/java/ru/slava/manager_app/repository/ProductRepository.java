package ru.slava.manager_app.repository;

import java.util.List;
import java.util.Optional;

import ru.slava.manager_app.entity.Product;

public interface ProductRepository {

    List<Product> findAll();

    Product save(Product product);

    Optional<Product> findById(int productId);
    
}
