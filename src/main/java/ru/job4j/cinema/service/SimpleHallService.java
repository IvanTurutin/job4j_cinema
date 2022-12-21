package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.repository.HallRepository;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /**
     * Производит обработку запроса при добавлении кинозала в репозиторий
     * @param hall объект кинозала, который требуется добавить в репозиторий
     * @return если кинозал добавлен успешно - возвращается объект Hall в обертке Optional,
     * если не добавлен то Optional.empty()
     */
    @Override
    public Optional<Hall> add(Hall hall) {
        return hallRepository.add(hall);
    }

    /**
     * Производит обработку запроса при поиске кинозала по идентификтору
     * @param id идентификатор кинозала
     * @return если кинозал найден - возвращается объект Hall в обертке Optional,
     * если не найден то Optional.empty()
     */
    @Override
    public Optional<Hall> findById(int id) {
        return hallRepository.findById(id);
    }

    /**
     * Производит обработку запроса при поиске всех кинозалов
     * @return карту с найденными кинозалами, где ключем является идентификатор кинозала
     */
    @Override
    public Map<Integer, Hall> findAll() {
        return hallRepository.findAll().stream().collect(Collectors.toMap(Hall::getId, e -> e));
    }

    /**
     * Производит обработку запроса на обновление данных кинозала
     * @param hall объект кинозала с обновляемыми данными
     * @return true если обнвлено успешно, false есил не обновлено
     */
    @Override
    public boolean update(Hall hall) {
        return hallRepository.update(hall);
    }

    /**
     * Производит обработку запроса на удаление кинозала
     * @param id идентификтор киноазала, который требуется удалить
     * @return удаленный кинозал обернутый в Optional при успешном удалении, и Optional.empty() если кинозал не удален.
     */
    @Override
    public Optional<Hall> delete(int id) {
        return hallRepository.delete(id);
    }
}
