package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Хранилище билетов
 * @see ru.job4j.cinema.model.Ticket
 */
public interface TicketRepository {

    /**
     * Метод осуществляет добавление билета в базу данных. При этом билету присваивается уникальный идентификатор.
     * @param ticket объект билета.
     * @return объект с присвоинным идентификатором если добавление билета произошло успешно,
     * либо Optional.empty(), если место сеанса уже занято.
     */
    Optional<Ticket> add(Ticket ticket);

    /**
     * Осуществляет поиск билетов по пользователю, забронировавшему их.
     * @param user объект пользователя, для которого требуется найти билеты.
     * @return список билетов если билеты найдены, либо пустой список, если билеты не найдены.
     */
    List<Ticket> findByUser(User user);

    /**
     * Осуществляет поиск билетов по идентификатору билета.
     * @param id иденитификатор билета
     * @return возварщает объект билета обернутый в Optional если билет найден,
     * либо Optional.empty() если билет не найден.
     */
    Optional<Ticket> findById(int id);

    /**
     * Удаляет билет из базы
     * @param id идентификатор билета
     * @return удаленный билет обернутый в Optional, либо Optional.empty() если произошла обшибка удаления.
     */
    Optional<Ticket> delete(int id);
}
