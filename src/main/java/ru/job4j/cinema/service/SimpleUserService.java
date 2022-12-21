package ru.job4j.cinema.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.repository.UserRepository;

import java.util.List;
import java.util.Optional;

/**
 * Сервисный слой пользователей
 */
@ThreadSafe
@Service
public class SimpleUserService implements UserService {
    private final UserRepository userRepository;

    public SimpleUserService(UserRepository userDBStore) {
        this.userRepository = userDBStore;
    }

    /**
     * Производит обработку запроса при добавлении пользователя в репозиторий
     * @param user объект пользователя, который требуется добавить в репозиторий
     * @return если пользователь добавлен успешно - возвращается объект User в обертке Optional,
     * если не добавлен то Optional.empty()
     */
    @Override
    public Optional<User> add(User user) {
        return userRepository.add(user);
    }

    /**
     * Производит обработку запроса при поиске пользователя по почте и паролю
     * @param email почта пользователя
     * @param password пароль пользователя
     * @return если пользователь найден - возвращается объект User в обертке Optional,
     * если не найден то Optional.empty()
     */
    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return userRepository.findByEmailAndPassword(email, password);
    }

    /**
     * Производит обработку запроса на удаление пользователя
     * @param id идентификтор пользователя, которого требуется удалить
     * @return удаленный пользователь обернутый в Optional при успешном удалении,
     * и Optional.empty() если пользователь не удален.
     */
    @Override
    public Optional<User> delete(int id) {
        return userRepository.delete(id);
    }

    /**
     * Производит обработку запроса на обновление данных пользователя
     * @param user объект пользователя с обновляемыми данными
     * @return true если обнвлено успешно, false если не обновлено
     */
    @Override
    public boolean update(User user) {
        return userRepository.update(user);
    }

    /**
     * Производит обработку запроса при поиске всех пользователей
     * @return список с найденными пользователями
     */
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

}
