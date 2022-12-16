package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Session;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class JdbcHallRepositoryTest {

    private static JdbcHallRepository store;

    @BeforeAll
    public static void initStore() {
        store = new JdbcHallRepository(new Main().loadPool());
        store.truncateTable();
    }

    @AfterEach
    public void truncateTable() {
        store.truncateTable();
    }

    @Test
    void whenFindById() {
        Hall hall = new Hall(0, "first", 5, 10);
        store.add(hall);
        Optional<Hall> hallInDB = store.findById(hall.getId());
        assertThat(hall.getId()).isNotEqualTo(0);
        assertThat(hallInDB.isPresent()).isTrue();
        assertThat(hallInDB.get().getName()).isEqualTo(hall.getName());
    }

    @Test
    void whenFindAll() {
        Hall hall1 = new Hall(0, "first", 5, 10);
        Hall hall2 = new Hall(0, "second", 8, 15);
        store.add(hall1);
        store.add(hall2);
        Map.Entry<Integer, Hall> entry1 = new AbstractMap.SimpleEntry<>(hall1.getId(), hall1);
        Map.Entry<Integer, Hall> entry2 = new AbstractMap.SimpleEntry<>(hall2.getId(), hall2);
        Map<Integer, Hall> sessions = store.findAll();
        assertThat(sessions).isNotEmpty().hasSize(2).contains(entry1, entry2);
    }

}