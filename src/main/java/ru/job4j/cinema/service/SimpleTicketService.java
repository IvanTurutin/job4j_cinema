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
    private final TicketRepository ticketRepository;

    public SimpleTicketService(TicketRepository ticketDBStore) {
        this.ticketRepository = ticketDBStore;
    }

    /**
     * Производит обработку запроса при добавлении билета в репозиторий
     * @param ticket объект билета, который требуется добавить в репозиторий
     * @return если билет добавлен успешно - возвращается объект Ticket в обертке Optional,
     * если не добавлен то Optional.empty()
     */
    @Override
    public Optional<Ticket> add(Ticket ticket) {
        return ticketRepository.add(ticket);
    }

    /**
     * Производит обработку запроса при поиске билета по пользователю
     * @param user пользователь, купивший билет
     * @return если билет найден - возвращается объект Ticket в обертке Optional,
     * если не найден то Optional.empty()
     */
    @Override
    public List<Ticket> findByUser(User user) {
        return ticketRepository.findByUser(user);
    }

    /**
     * Производит обработку запроса при поиске билета по идентификтору
     * @param id идентификатор билета
     * @return если билет найден - возвращается объект Ticket в обертке Optional,
     * если не найден то Optional.empty()
     */
    @Override
    public Optional<Ticket> findById(int id) {
        return ticketRepository.findById(id);
    }

    /**
     * Производит обработку запроса на удаление билета
     * @param id идентификтор билета, который требуется удалить
     * @return удаленный билет обернутый в Optional при успешном удалении, и Optional.empty() если билет не удален.
     */
    @Override
    public Optional<Ticket> delete(int id) {
        return ticketRepository.delete(id);
    }
}
