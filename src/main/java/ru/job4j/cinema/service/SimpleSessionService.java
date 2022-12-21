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
    private final SessionRepository sessionRepository;

    public SimpleSessionService(SessionRepository sessionDBStore) {
        this.sessionRepository = sessionDBStore;
    }

    /**
     * Производит обработку запроса при поиске всех сеансов
     * @return коллекцию с найденными кинозалами
     */
    @Override
    public Collection<Session> findAll() {
        return sessionRepository.findAll();
    }

    /**
     * Производит обработку запроса при поиске сеанса по идентификтору
     * @param id идентификатор сеанса
     * @return если сеанс найден - возвращается объект Session в обертке Optional,
     * если не найден то Optional.empty()
     */
    @Override
    public Optional<Session> findById(int id) {
        return sessionRepository.findById(id);
    }

    /**
     * Производит обработку запроса при добавлении сеанса в репозиторий
     * @param session объект сеанса, который требуется добавить в репозиторий
     * @return если сеанс добавлен успешно - возвращается объект Session в обертке Optional,
     * если не добавлен то Optional.empty()
     */
    @Override
    public Optional<Session> add(Session session) {
        return sessionRepository.add(session);
    }

    /**
     * Производит обработку запроса на обновление данных сеанса
     * @param session объект сеанса с обновляемыми данными
     * @return true если обнвлено успешно, false если не обновлено
     */
    @Override
    public boolean update(Session session) {
        return sessionRepository.update(session);
    }

    /**
     * Производит обработку запроса на удаление сеанса
     * @param id идентификтор сеанса, который требуется удалить
     * @return удаленный сеанс обернутый в Optional при успешном удалении, и Optional.empty() если сеанс не удален.
     */
    @Override
    public Optional<Session>  delete(int id) {
        return sessionRepository.delete(id);
    }
}
