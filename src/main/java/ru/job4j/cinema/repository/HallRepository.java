package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Hall;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

/**
 * Хранилище залов
 * @see ru.job4j.cinema.model.Hall
 */
public interface HallRepository {
    /**
     * Метод производит добавление кинозала в базу данных с присвоением идентификатора
     * @param hall объект кинозала, добавляемый в базу данных
     * @return добавленный кинозал с обновленным идентификатором обернутый в Optional,
     * либо Optional.empty() если такого сеанса не найдено.
     */
    Optional<Hall> add(Hall hall);
    /**
     * Метод осуществляет поиск кинозала по его идентификатору.
     * @param id идентификатор искомого кинозала
     * @return кинозал обернутый в Optional, либо Optional.empty() если такого кинозала не найдено.
     */
    Optional<Hall> findById(int id);
    /**
     * Метод осуществляет поиск всех имеющихся кинозалов.
     * @return карту кинозалов.
     */
    Collection<Hall> findAll();
    /**
     * Обновляет информацию о кинозале
     * @param hall объект кинозала, на который требуется обновить существующий
     * @return true если данные были обновлены, или false если данные не были обновлены.
     */
    boolean update(Hall hall);
    /**
     * Удаляет кинозал из базы данных
     * @param id идентификатор кинозала, который нужно удалить
     * @return Optional с удаленным кинозалом, или Optional.empty() если кинозал не был удален.
     */
    Optional<Hall> delete(int id);

}
