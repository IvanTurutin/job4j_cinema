package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Session;

import java.util.Collection;
import java.util.Optional;

public interface SessionService {
    Optional<Session> add(Session session);

    Collection<Session> findAll();

    Optional<Session> findById(int id);

    Optional<Session> update(Session session);

    Optional<Session> delete(int id);
}
