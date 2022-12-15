package ru.job4j.cinema.repository;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Session;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Хранилище сеансов
 * @see ru.job4j.cinema.model.Session
 */
@ThreadSafe
@Repository
public class JdbcSessionRepository implements SessionRepository {
    private final DataSource pool;
    private static final Logger LOG = LoggerFactory.getLogger(JdbcSessionRepository.class.getName());
    private static final String TABLE_NAME_SESSIONS = "sessions";
    private static final String TABLE_NAME_HALLS = "halls";
    private static final String SELECT_STATEMENT = String.format(
            "SELECT s.*, "
                    + "h.name as hall_name, "
                    + "h.rows, "
                    + "h.cells "
                    + "FROM %s as s "
                    + "JOIN %s as h ON s.hall_id = h.id ",
            TABLE_NAME_SESSIONS,
            TABLE_NAME_HALLS);
    private static final String FIND_ALL_STATEMENT = SELECT_STATEMENT + "ORDER BY id";
    private static final String FIND_BY_ID_STATEMENT = SELECT_STATEMENT + "WHERE s.id = ?";
    private static final String ADD_STATEMENT = String.format(
            "INSERT INTO %s(name, hall_id) VALUES (?, ?)",
            TABLE_NAME_SESSIONS);

    private final static String TRUNCATE_TABLE = String.format(
            "DELETE FROM %s",
            TABLE_NAME_SESSIONS
    );
    public JdbcSessionRepository(DataSource pool) {
        this.pool = pool;
    }

    /**
     * Метод осуществляет поиск всех имеющихся сеансов.
     * @return список сеансов.
     */
    @Override
    public Collection<Session> findAll() {
        List<Session> sessions = new LinkedList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_ALL_STATEMENT)
        ) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    sessions.add(createSession(rs));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in JdbcSessionRepository", e);
        }
        return sessions;
    }

    /**
     * Метод осуществляет поиск сеанса по его идентификатору.
     * @param id идентификатор искомого сеанса
     * @return сеанс обернутый в Optional, либо Optional.empty() если такого сеанса не найдено.
     */
    @Override
    public Optional<Session> findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_BY_ID_STATEMENT)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return Optional.ofNullable(createSession(it));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in PostDBStore", e);
        }
        return Optional.empty();
    }

    /**
     * Метод создает сеанс из передаваемого ResultSet
     * @param rs ResultSet с данными, полученными из базы данных
     * @return сформированный объект Session при успешном выполнении,
     * либо null, если имеются ошибки доступа к базе данных.
     */
    private Session createSession(ResultSet rs) {
        try {
            return new Session(
                    rs.getInt("id"),
                    rs.getString("name"),
                    new Hall(rs.getInt("hall_id"),
                            rs.getString("hall_name"),
                            rs.getInt("rows"),
                            rs.getInt("cells")
                    )
            );
        } catch (SQLException e) {
            LOG.error("Exception in JdbcSessionRepository", e);
        }
        return null;
    }

    /**
     * Метод очищает таблицу sssions
     */
    public void truncateTable() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(TRUNCATE_TABLE)
        ) {
            ps.execute();
        } catch (Exception e) {
            LOG.error("Exception in JdbcSessionRepository", e);
        }
    }

    /**
     * Метод производит добавление сеанса в базу данных с присвоением идентификатора
     * @param session объект сеанса, добавляемый в базу данных
     * @return добавленный сеанс с обноенным идентификатором обернутый в Optional,
     * либо Optional.empty() если такого сеанса не найдено.
     */
    @Override
    public Optional<Session> add(Session session) {
        Optional<Session> optionalSession = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(ADD_STATEMENT,
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, session.getName());
            ps.setInt(2, session.getHall().getId());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    session.setId(rs.getInt(1));
                    optionalSession = Optional.of(session);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in JdbcSessionRepository", e);
        }
        return optionalSession;
    }

    /**
     * Метод осуществляет обновледние данных сеанса в базе данных
     * @param session объект сеанса для обновления.
     * @return обновленный сеанс обернутый в Optional, либо Optional.empty() если такого сеанса не найдено.
     */
    @Override
    public Optional<Session> update(Session session) {
        throw new UnsupportedOperationException("Not implemented, yet");
    }

    /**
     * Метод осуществляет удаление сеанса из базы данных
     * @param id идентификатор сеанса для удаления.
     * @return удаленный сеанс обернутый в Optional, либо Optional.empty() если произошла обшибка удаления.
     */
    @Override
    public Optional<Session> delete(int id) {
        throw new UnsupportedOperationException("Not implemented, yet");
    }
}
