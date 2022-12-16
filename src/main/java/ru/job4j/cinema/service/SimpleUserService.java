package ru.job4j.cinema.service;

import jdk.jshell.spi.ExecutionControl;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Сервисный слой сеансов
 */
@ThreadSafe
@Service
public class SimpleUserService implements UserService {
    private final UserRepository userDBStore;

    public SimpleUserService(UserRepository userDBStore) {
        this.userDBStore = userDBStore;
    }

    @Override
    public Optional<User> add(User user) {
        return userDBStore.add(user);
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return userDBStore.findByEmailAndPassword(email, password);
    }

    @Override
    public Optional<User> delete(int id) {
        return userDBStore.delete(id);
    }

    @Override
    public boolean update(User user) {
        return userDBStore.update(user);
    }

    @Override
    public List<User> findAll() {
        return userDBStore.findAll();
    }

}
