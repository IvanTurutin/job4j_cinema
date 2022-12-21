package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Session;

import java.util.Collection;
import java.util.Optional;

/**
 * Сервисный слой сеансов
 */
public interface SessionService {
    /**
     * Производит обработку запроса при добавлении сеанса в репозиторий
     * @param session объект сеанса, который требуется добавить в репозиторий
     * @return если сеанс добавлен успешно - возвращается объект Session в обертке Optional,
     * если не добавлен то Optional.empty()
     */
    Optional<Session> add(Session session);

    /**
     * Производит обработку запроса при поиске всех сеансов
     * @return коллекцию с найденными кинозалами
     */
    Collection<Session> findAll();

    /**
     * Производит обработку запроса при поиске сеанса по идентификтору
     * @param id идентификатор сеанса
     * @return если сеанс найден - возвращается объект Session в обертке Optional,
     * если не найден то Optional.empty()
     */
    Optional<Session> findById(int id);

    /**
     * Производит обработку запроса на обновление данных сеанса
     * @param session объект сеанса с обновляемыми данными
     * @return true если обнвлено успешно, false если не обновлено
     */
    boolean update(Session session);

    /**
     * Производит обработку запроса на удаление сеанса
     * @param id идентификтор сеанса, который требуется удалить
     * @return удаленный сеанс обернутый в Optional при успешном удалении, и Optional.empty() если сеанс не удален.
     */
    Optional<Session> delete(int id);
}
