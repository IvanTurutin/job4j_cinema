package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.model.User;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class PostgresUserRepositoryTest {

    private static JDBCUserRepository store;

    @BeforeAll
    public static void initStore() {
        store = new JDBCUserRepository(new Main().loadPool());
        store.truncateTable();
    }

    @AfterEach
    public void truncateTable() {
        store.truncateTable();
    }

    @Test
    void whenFindByEmailAndPassword() {
        User user = new User(
                0,
                "Name",
                "123456789",
                "ivan@email.com",
                "1234"
                );
        store.add(user);
        Optional<User> userInDb = store.findByEmailAndPassword(user.getEmail(), user.getPassword());
        assertThat(userInDb.isPresent()).isTrue();
        assertThat(userInDb.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void whenRepeatEmail() {
        User user = new User(
                0,
                "Name",
                "123456789",
                "ivan@email.com",
                "1234"
        );
        User user2 = new User(
                0,
                "Name2",
                "1234567890",
                "ivan@email.com",
                "12345"
        );
        store.add(user);
        Optional<User> user2InDB = store.add(user2);
        assertThat(user2InDB.isEmpty()).isTrue();
    }

    @Test
    void whenRepeatPhone() {
        User user = new User(
                0,
                "Name",
                "123456789",
                "ivan@email.com",
                "1234"
        );
        User user2 = new User(
                0,
                "Name2",
                "1234567890",
                "ivan@email.ru",
                "1234"
        );
        store.add(user);
        Optional<User> user2InDB = store.add(user2);
        assertThat(user2InDB.isEmpty()).isTrue();
    }

}