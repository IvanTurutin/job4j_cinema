package ru.job4j.cinema.service;

import ru.job4j.cinema.model.Hall;

import java.util.Map;
import java.util.Optional;

/**
 * Сервисный слой кинозалов
 */
public interface HallService {
    /**
     * Производит обработку запроса при добавлении кинозала в репозиторий
     * @param hall объект кинозала, который требуется добавить в репозиторий
     * @return если кинозал добавлен успешно - возвращается объект Hall в обертке Optional,
     * если не добавлен то Optional.empty()
     */
    Optional<Hall> add(Hall hall);

    /**
     * Производит обработку запроса при поиске кинозала по идентификтору
     * @param id идентификатор кинозала
     * @return если кинозал найден - возвращается объект Hall в обертке Optional,
     * если не найден то Optional.empty()
     */
    Optional<Hall> findById(int id);

    /**
     * Производит обработку запроса при поиске всех кинозалов
     * @return карту с найденными кинозалами, где ключем является идентификатор кинозала
     */
    Map<Integer, Hall> findAll();

    /**
     * Производит обработку запроса на обновление данных кинозала
     * @param hall объект кинозала с обновляемыми данными
     * @return true если обнвлено успешно, false есил не обновлено
     */
    boolean update(Hall hall);

    /**
     * Производит обработку запроса на удаление кинозала
     * @param id идентификтор киноазала, который требуется удалить
     * @return удаленный кинозал обернутый в Optional при успешном удалении, и Optional.empty() если кинозал не удален.
     */
    Optional<Hall> delete(int id);
}
