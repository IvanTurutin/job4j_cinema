package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Session;

import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class PostgresSessionRepositoryTest {

    private static JDBCSessionRepository store;

    @BeforeAll
    public static void initStore() {
        store = new JDBCSessionRepository(new Main().loadPool());
        store.truncateTable();
    }

    @AfterEach
    public void truncateTable() {
        store.truncateTable();
    }

    @Test
    void whenFindById() {
        Session session = new Session(
                0,
                "Session 1",
                new Hall(1, "", 1, 2)
        );
        store.add(session);
        Optional<Session> sessionInDB = store.findById(session.getId());
        assertThat(sessionInDB.isPresent()).isTrue();
        assertThat(sessionInDB.get().getName()).isEqualTo(session.getName());
    }

    @Test
    void whenFindAll() {
        Session session = new Session(
                0,
                "Session 1",
                new Hall(1, "", 1, 2)
        );
        Session session2 = new Session(
                0,
                "Session 2",
                new Hall(2, "", 1, 2)
        );
        store.add(session);
        store.add(session2);
        Collection<Session> sessions = store.findAll();
        assertThat(sessions).isNotEmpty().hasSize(2).contains(session, session2);
    }
}