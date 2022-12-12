package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.User;

import java.util.Collection;
import java.util.Optional;

public interface SessionRepository {

    Optional<Session> add(Session session);

    Collection<Session> findAll();

    Optional<Session> findById(int id);

    Optional<Session> update(Session session);

    Optional<Session> delete(int id);
}
