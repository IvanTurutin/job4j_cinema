package ru.job4j.cinema.repository;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Хранилище пользователей
 * @see ru.job4j.cinema.model.User
 */
@ThreadSafe
@Repository
public class JdbcUserRepository implements UserRepository {

    private final DataSource pool;
    private static final Logger LOG = LoggerFactory.getLogger(JdbcUserRepository.class.getName());

    private static final String TABLE_NAME = "users";
    private static final String ADD_STATEMENT = String.format(
            "INSERT INTO %s(username, password, email, phone) VALUES (?, ?, ?, ?)",
            TABLE_NAME);
    private static final String TRUNCATE_TABLE = String.format("DELETE FROM %s", TABLE_NAME);
    private static final String FIND_BY_EMAIL_AND_PASSWORD_STATEMENT = String.format(
            "SELECT * FROM %s WHERE email = ? AND password = ?",
            TABLE_NAME);


    public JdbcUserRepository(DataSource pool) {
        this.pool = pool;
    }

    /**
     * Добавляет пользователя в базу данных с присвоением объекту идентификатора.
     * @param user объект пользователя, который необходимо добавить.
     * @return объект пользователя с присвоенным идентификатором обернутым в Optional, если пользователь успешно добавлен,
     * либо Optional.empty() если пользователь не добавлен.
     */
    @Override
    public Optional<User> add(User user) {
        Optional<User> optionalUser = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(ADD_STATEMENT,
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPhone());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                    optionalUser = Optional.of(user);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in JdbcUserRepository", e);
        }
        return optionalUser;
    }

    /**
     * Поиск пользователя по почте и паролю
     * @param email почта пользователя
     * @param password пароль пользователя
     * @return пользователя обернутого в Optional, если пользователь найден,
     * либо Optional.empty() если пользователь не найден.
     */
    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps =  cn.prepareStatement(FIND_BY_EMAIL_AND_PASSWORD_STATEMENT)) {
            ps.setString(1, email);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createUser(rs));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in JdbcUserRepository", e);
        }
        return Optional.empty();
    }

    /**
     * Формирует объект User из данных полученных в ResultSet
     * @param rs результат запроса
     * @return объект User
     * @throws SQLException выбрасывается исключение в случае отсутствия необходимых данных в ResultSet
     */
    private User createUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("id"),
                rs.getString("username"),
                rs.getString("password"),
                rs.getString("email"),
                rs.getString("phone")
        );
    }

    /**
     * Очицает таблицу users
     */
    public void truncateTable() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(TRUNCATE_TABLE)
        ) {
            ps.execute();
        } catch (Exception e) {
            LOG.error("Exception in JdbcUserRepository", e);
        }
    }

    /**
     * Удаляет пользователя из базы данных
     * @param id идентификатор пользователя, которого нужно удалить
     * @return Optional с удаленным пользователем, или Optional.empty() если пользователь не был удален.
     */
    @Override
    public Optional<User> delete(int id) {
        throw new UnsupportedOperationException("Not implemented, yet");
    }

    /**
     * Обновляет пользователя в базе данных
     * @param user объект пользователя, который нужно обновить
     * @return Optional с обновленным пользователем, или Optional.empty() если пользователь не был обновлен.
     */
    @Override
    public boolean update(User user) {
        throw new UnsupportedOperationException("Not implemented, yet");
    }

    /**
     * Находит всех пользователей, имеющихся в базе данных
     * @return Список пользоваетелей.
     */
    @Override
    public List<User> findAll() {
        throw new UnsupportedOperationException("Not implemented, yet");
    }
}
