package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    Optional<Ticket> add(Ticket ticket);

    List<Ticket> findByUser(User user);

    Optional<Ticket> findById(int id);

    boolean delete(int id);
}
