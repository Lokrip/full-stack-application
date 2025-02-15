package ru.slava.manager_app.service;

import java.util.List;
import java.util.Optional;

import ru.slava.manager_app.entity.Product;

public interface ProductService {
    List<Product> findAllProducts();

    Product createProduct(String title, String details);

    Optional<Product> findProduct(int productId);

    void updateProduct(Integer id, String title, String details);

    void deleteProduct(Integer id);
}
