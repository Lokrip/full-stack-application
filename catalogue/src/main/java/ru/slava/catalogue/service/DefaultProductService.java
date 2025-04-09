package ru.slava.catalogue.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import ru.slava.catalogue.entity.Product;
import ru.slava.catalogue.repository.ProductRepository;

@Service
public class DefaultProductService implements ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public DefaultProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Iterable<Product> findAllProducts(String filter) {
        if (filter != null && !filter.isBlank()) {
            String newFilter = "%" + filter + "%";
            return this.productRepository.namedSqlGetAllProductByTitle(newFilter);
        }
        return this.productRepository.findAll();
    }

    @Override
    public Product createProduct(String title, String details) {
       return this.productRepository.save(new Product(null, title, details));
    }

    @Override
    public Optional<Product> findProduct(int productId) {
        return this.productRepository.findById(productId);
    }

    @Override
    @Transactional
    public void updateProduct(Integer id, String title, String details) {
        //этот код будет выполняться в рамке одной транзакций
        //тоесть ищем запись и если в этой запсии будут изменение то мы изменим продукт
        //и транзакция автоматический сохранит его
        this.productRepository.findById(id)
            .ifPresentOrElse(product -> {
                product.setTitle(title);
                product.setDetails(details);
            }, () -> {
                throw new NoSuchElementException();
            });
    }

    @Override
    @Transactional
    public void deleteProduct(Integer id) {
        this.productRepository.deleteById(id);
    }
}
