package ru.job4j.cinema.repository;

import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.User;

import java.util.Collection;
import java.util.Optional;

/**
 * Хранилище сеансов
 * @see ru.job4j.cinema.model.Session
 */
public interface SessionRepository {

    /**
     * Метод производит добавление сеанса в базу данных с присвоением идентификатора
     * @param session объект сеанса, добавляемый в базу данных
     * @return добавленный сеанс с обновленным идентификатором обернутый в Optional,
     * либо Optional.empty() если такого сеанса не найдено.
     */
    Optional<Session> add(Session session);

    /**
     * Метод осуществляет поиск всех имеющихся сеансов.
     * @return список сеансов.
     */
    Collection<Session> findAll();

    /**
     * Метод осуществляет поиск сеанса по его идентификатору.
     * @param id идентификатор искомого сеанса
     * @return сеанс обернутый в Optional, либо Optional.empty() если такого сеанса не найдено.
     */
    Optional<Session> findById(int id);

    /**
     * Метод осуществляет обновледние данных сеанса в базе данных
     * @param session объект сеанса для обновления.
     * @return true если сеанс успешно обновлен, либо false если обновления не произошло.
     */
    boolean update(Session session);

    /**
     * Метод осуществляет удаление сеанса из базы данных
     * @param id идентификатор сеанса для удаления.
     * @return удаленный сеанс обернутый в Optional, либо Optional.empty() если произошла обшибка удаления.
     */
    Optional<Session> delete(int id);
}
