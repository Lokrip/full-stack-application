package ru.slava.manager_app.repository;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import ru.slava.manager_app.entity.Product;

@Component
public class InMemoryProductRepository implements ProductRepository {
    //Collections.synchronizedList — это метод в Java, который оборачивает обычный список (List) в потокобезопасный список. Он синхронизирует все методы списка, чтобы предотвратить возникновение состояний гонки, когда несколько потоков пытаются одновременно модифицировать или читать данные из списка.
    //Это означает, что один поток будет блокировать доступ к коллекции, пока не завершит свою операцию, а другие потоки, пытающиеся получить доступ к той же коллекции в это время, будут вынуждены ожидать.
    private final List<Product> products = Collections.synchronizedList(new LinkedList<>());
    
    // //Блок кода, который находится внутри инициализатора экземпляра (или instance initializer), вызывается при каждом создании объекта класса, до конструктора
    // {IntStream
    //     .range(1, 4)
    //     .forEach(i -> this.products.add(
    //         new Product(
    //             i,
    //             "Товар №%d".formatted(i),
    //             "Описание №%d".formatted(i)
    //         )
    //     ));}

    @Override
    public List<Product> findAll() {
        //unmodifiableList создать не модефицированную копию списка это нужны
        //чтобы пользователь при получение списка продутов не смогу на прямую ее поменять

        //Метод Collections.unmodifiableList возвращает неизменяемую 
        //(или "только для чтения") версию списка. Это значит, что пользователи, 
        //получающие этот список, не смогут изменять его содержимое напрямую, 
        //например, добавлять, удалять или изменять элементы.
        // Создает неизменяемую версию списка
        return Collections.unmodifiableList(this.products);
    }

    @Override
    public Product save(Product product) {
        product.setId(this.products.stream()
        .max(Comparator.comparingInt(Product::getId))
        .map(Product::getId)
        .orElse(0) + 1);
        this.products.add(product);
        return product;
    }

    @Override
    public Optional<Product> findById(int productId) {
        return this.products.stream()
            .filter(product -> Objects.equals(productId, product.getId()))
            .findFirst();
    }
}
