package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Hall;

import java.util.Map;
import java.util.Optional;

public interface HallService {
    Optional<Hall> add(Hall hall);
    Optional<Hall> findById(int id);
    Map<Integer, Hall> findAll();
    boolean update(Hall hall);
    Optional<Hall> delete(int id);
}
