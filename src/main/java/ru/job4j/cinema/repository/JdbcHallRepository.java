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
 * Хранилище залов
 * @see ru.job4j.cinema.model.Hall
 */
@ThreadSafe
@Repository
public class JdbcHallRepository implements HallRepository {

    private final DataSource pool;
    private static final Logger LOG = LoggerFactory.getLogger(JdbcHallRepository.class.getName());
    private static final String TABLE_NAME_HALLS = "halls";
    private static final String SELECT_STATEMENT = String.format(
            "SELECT * FROM %s ", TABLE_NAME_HALLS);
    private static final String FIND_ALL_STATEMENT = SELECT_STATEMENT + "ORDER BY id";
    private static final String FIND_BY_ID_STATEMENT = SELECT_STATEMENT + "WHERE id = ?";
    private static final String ADD_STATEMENT = String.format(
            "INSERT INTO %s(name, rows, cells) VALUES (?, ?, ?)",
            TABLE_NAME_HALLS);
    private static final String TRUNCATE_TABLE = String.format(
            "DELETE FROM %s",
            TABLE_NAME_HALLS
    );

    public JdbcHallRepository(DataSource pool) {
        this.pool = pool;
    }

    /**
     * Метод производит добавление кинозала в базу данных с присвоением идентификатора
     * @param hall объект кинозала, добавляемый в базу данных
     * @return добавленный кинозал с обновленным идентификатором обернутый в Optional,
     * либо Optional.empty() если такого сеанса не найдено.
     */
    @Override
    public Optional<Hall> add(Hall hall) {
        Optional<Hall> optionalHall = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(ADD_STATEMENT,
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, hall.getName());
            ps.setInt(2, hall.getRows());
            ps.setInt(3, hall.getCells());
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    hall.setId(rs.getInt(1));
                    optionalHall = Optional.of(hall);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in JdbcHallRepository", e);
        }
        return optionalHall;
    }

    /**
     * Метод осуществляет поиск кинозала по его идентификатору.
     * @param id идентификатор искомого кинозала
     * @return кинозал обернутый в Optional, либо Optional.empty() если такого кинозала не найдено.
     */
    @Override
    public Optional<Hall> findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_BY_ID_STATEMENT)
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return createHall(it);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in JdbcHallRepository", e);
        }
        return Optional.empty();
    }

    /**
     * Метод осуществляет поиск всех имеющихся кинозалов.
     * @return карту кинозалов.
     */
    @Override
    public Collection<Hall> findAll() {
        Collection<Hall> halls = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(FIND_ALL_STATEMENT)
        ) {
            try (ResultSet rs = ps.executeQuery()) {
                Optional<Hall> optionalHall;
                Hall hall;
                while (rs.next()) {
                    optionalHall = createHall(rs);
                    if (optionalHall.isPresent()) {
                        hall = optionalHall.get();
                        halls.add(hall);
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in JdbcHallRepository", e);
        }
        return halls;
    }

    /**
     * Метод создает кинозал из передаваемого ResultSet
     * @param rs ResultSet с данными, полученными из базы данных
     * @return сформированный объект Hall обернутый в Optional при успешном выполнении,
     * либо Optional.empty(), если имеются ошибки доступа к базе данных.
     */
    private Optional<Hall> createHall(ResultSet rs) {
        try {
            return Optional.of(new Hall(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("rows"),
                    rs.getInt("cells")
            ));
        } catch (SQLException e) {
            LOG.error("Exception in JdbcHallRepository", e);
        }
        return Optional.empty();
    }

    /**
     * Обновляет информацию о кинозале
     * @param hall объект кинозала, на который требуется обновить существующий
     * @return true если данные были обновлены, или false если данные не были обновлены.
     */
    @Override
    public boolean update(Hall hall) {
        throw new UnsupportedOperationException("Not implemented, yet");
    }

    /**
     * Удаляет кинозал из базы данных
     * @param id идентификатор кинозала, который нужно удалить
     * @return Optional с удаленным кинозалом, или Optional.empty() если кинозал не был удален.
     */
    @Override
    public Optional<Hall> delete(int id) {
        throw new UnsupportedOperationException("Not implemented, yet");
    }

    /**
     * Метод очищает таблицу halls
     */
    public void truncateTable() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(TRUNCATE_TABLE)
        ) {
            ps.execute();
        } catch (Exception e) {
            LOG.error("Exception in JdbcHallRepository", e);
        }
    }

}
