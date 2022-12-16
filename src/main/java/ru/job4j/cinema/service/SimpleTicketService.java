package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.TicketRepository;

import java.util.List;
import java.util.Optional;

/**
 * Сервисный слой сеансов
 */
@ThreadSafe
@Service
public class SimpleTicketService implements TicketService {
    private final TicketRepository ticketDBStore;

    public SimpleTicketService(TicketRepository ticketDBStore) {
        this.ticketDBStore = ticketDBStore;
    }

    @Override
    public Optional<Ticket> add(Ticket ticket) {
        return ticketDBStore.add(ticket);
    }

    @Override
    public List<Ticket> findByUser(User user) {
        return ticketDBStore.findByUser(user);
    }

    @Override
    public Optional<Ticket> findById(int id) {
        return ticketDBStore.findById(id);
    }

    @Override
    public boolean delete(int id) {
        return ticketDBStore.delete(id);
    }
}
