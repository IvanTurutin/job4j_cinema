package ru.job4j.cinema.service;

import ru.job4j.cinema.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Сервисный слой пользователей
 */
public interface UserService {
    /**
     * Производит обработку запроса при добавлении пользователя в репозиторий
     * @param user объект пользователя, который требуется добавить в репозиторий
     * @return если пользователь добавлен успешно - возвращается объект User в обертке Optional,
     * если не добавлен то Optional.empty()
     */
    Optional<User> add(User user);

    /**
     * Производит обработку запроса на удаление пользователя
     * @param id идентификтор пользователя, которого требуется удалить
     * @return удаленный пользователь обернутый в Optional при успешном удалении,
     * и Optional.empty() если пользователь не удален.
     */
    Optional<User> delete(int id);

    /**
     * Производит обработку запроса на обновление данных пользователя
     * @param user объект пользователя с обновляемыми данными
     * @return true если обнвлено успешно, false если не обновлено
     */
    boolean update(User user);

    /**
     * Производит обработку запроса при поиске пользователя по почте и паролю
     * @param email почта пользователя
     * @param password пароль пользователя
     * @return если пользователь найден - возвращается объект User в обертке Optional,
     * если не найден то Optional.empty()
     */
    Optional<User> findByEmailAndPassword(String email, String password);

    /**
     * Производит обработку запроса при поиске всех пользователей
     * @return список с найденными пользователями
     */
    List<User> findAll();
}
