package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Hall;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface HallRepository {
    Optional<Hall> add(Hall hall);
    Optional<Hall> findById(int id);
    Collection<Hall> findAll();
    boolean update(Hall hall);
    Optional<Hall> delete(int id);

}
