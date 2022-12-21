package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Сервисный слой билетов
 */
public interface TicketService {
    /**
     * Производит обработку запроса при добавлении билета в репозиторий
     * @param ticket объект билета, который требуется добавить в репозиторий
     * @return если билет добавлен успешно - возвращается объект Ticket в обертке Optional,
     * если не добавлен то Optional.empty()
     */
    Optional<Ticket> add(Ticket ticket);

    /**
     * Производит обработку запроса при поиске билета по пользователю
     * @param user пользователь, купивший билет
     * @return если билет найден - возвращается объект Ticket в обертке Optional,
     * если не найден то Optional.empty()
     */
    List<Ticket> findByUser(User user);

    /**
     * Производит обработку запроса при поиске билета по идентификтору
     * @param id идентификатор билета
     * @return если билет найден - возвращается объект Ticket в обертке Optional,
     * если не найден то Optional.empty()
     */
    Optional<Ticket> findById(int id);

    /**
     * Производит обработку запроса на удаление билета
     * @param id идентификтор билета, который требуется удалить
     * @return удаленный билет обернутый в Optional при успешном удалении, и Optional.empty() если билет не удален.
     */
    Optional<Ticket> delete(int id);
}
