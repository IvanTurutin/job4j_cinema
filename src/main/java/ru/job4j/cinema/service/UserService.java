package ru.job4j.cinema.service;

import jdk.jshell.spi.ExecutionControl;
import ru.job4j.cinema.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Optional<User> add(User user);

    Optional<User> delete(int id);

    boolean update(User user);

    Optional<User> findByEmailAndPassword(String email, String password);

    List<User> findAll();
}
