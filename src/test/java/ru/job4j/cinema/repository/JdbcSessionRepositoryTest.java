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

class JdbcSessionRepositoryTest {

    private static JdbcSessionRepository store;
    private static JdbcHallRepository hallRepository;


    @BeforeAll
    public static void initStore() {
        store = new JdbcSessionRepository(new Main().loadPool());
        hallRepository = new JdbcHallRepository(new Main().loadPool());
        store.truncateTable();
        hallRepository.truncateTable();
    }

    @AfterEach
    public void truncateTable() {
        store.truncateTable();
        hallRepository.truncateTable();
    }

    @Test
    void whenFindById() {
        Hall hall = new Hall(0, "first", 5, 10);
        hallRepository.add(hall);
        Session session = new Session(
                0,
                "Session 1",
                hall.getId()
        );
        store.add(session);
        Optional<Session> sessionInDB = store.findById(session.getId());
        assertThat(sessionInDB.isPresent()).isTrue();
        assertThat(sessionInDB.get().getName()).isEqualTo(session.getName());
    }

    @Test
    void whenFindAll() {
        Hall hall1 = new Hall(0, "first", 5, 10);
        hallRepository.add(hall1);
        Hall hall2 = new Hall(0, "second", 8, 15);
        hallRepository.add(hall2);

        Session session = new Session(
                0,
                "Session 1",
                hall1.getId()
        );
        Session session2 = new Session(
                0,
                "Session 2",
                hall2.getId()
        );
        store.add(session);
        store.add(session2);
        Collection<Session> sessions = store.findAll();
        assertThat(sessions).isNotEmpty().hasSize(2).contains(session, session2);
    }
}