package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.HallRepository;

import java.util.Map;
import java.util.Optional;

/**
 * Сервисный слой кинозалов
 */
@ThreadSafe
@Service
public class SimpleHallService implements HallService {
    private final HallRepository hallRepository;

    public SimpleHallService(HallRepository hallRepository) {
        this.hallRepository = hallRepository;
    }

    @Override
    public Optional<Hall> add(Hall hall) {
        return hallRepository.add(hall);
    }

    @Override
    public Optional<Hall> findById(int id) {
        return hallRepository.findById(id);
    }

    @Override
    public Map<Integer, Hall> findAll() {
        return hallRepository.findAll();
    }

    @Override
    public boolean update(Hall hall) {
        return hallRepository.update(hall);
    }

    @Override
    public Optional<Hall> delete(int id) {
        return hallRepository.delete(id);
    }
}
