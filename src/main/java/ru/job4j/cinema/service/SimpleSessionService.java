package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.repository.SessionRepository;

import java.util.Collection;
import java.util.Optional;

/**
 * Сервисный слой сеансов
 */
@ThreadSafe
@Service
public class SimpleSessionService implements SessionService {
    private final SessionRepository sessionDBStore;

    public SimpleSessionService(SessionRepository sessionDBStore) {
        this.sessionDBStore = sessionDBStore;
    }

    @Override
    public Collection<Session> findAll() {
        return sessionDBStore.findAll();
    }

    @Override
    public Optional<Session> findById(int id) {
        return sessionDBStore.findById(id);
    }

    @Override
    public Optional<Session> add(Session session) {
        return sessionDBStore.add(session);
    }

    @Override
    public boolean update(Session session) {
        return sessionDBStore.update(session);
    }

    @Override
    public Optional<Session>  delete(int id) {
        return sessionDBStore.delete(id);
    }
}
