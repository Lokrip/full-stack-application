package ru.slava.manager_app.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import ru.slava.manager_app.entity.SelmagUser;

public interface SelmagUserRepository extends CrudRepository<SelmagUser, Integer> {
    Optional<SelmagUser> findByUsername(String username);
}
