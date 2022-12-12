package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {

    Optional<Ticket> add(Ticket ticket);

    List<Ticket> findByUser(User user);

    Optional<Ticket> findById(int id);

    Optional<Ticket> delete(int id);
}
