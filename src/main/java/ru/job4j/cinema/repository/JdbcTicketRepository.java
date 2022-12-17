package ru.job4j.cinema.repository;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Хранилище билетов
 * @see ru.job4j.cinema.model.Ticket
 */
@ThreadSafe
@Repository
public class JdbcTicketRepository implements TicketRepository {
    private final DataSource pool;
    private static final Logger LOG = LoggerFactory.getLogger(JdbcTicketRepository.class.getName());

    private static final String TABLE_NAME_TICKETS = "tickets";
    private static final String ADD_STATEMENT = String.format(
            "INSERT INTO %s(session_id, pos_row, cell, user_id) VALUES (?, ?, ?, ?)",
            TABLE_NAME_TICKETS);
    private static final String SELECT_TICKETS_STATEMENT = String.format(
            "SELECT * FROM %s ",
            TABLE_NAME_TICKETS);
    private static final String FIND_BY_USERID_STATEMENT = SELECT_TICKETS_STATEMENT + "WHERE user_id = ?";
    private static final String FIND_BY_SESSIONID_STATEMENT = SELECT_TICKETS_STATEMENT + "WHERE id = ?";
    private static final String TRUNCATE_TABLE =
            String.format("TRUNCATE TABLE %s RESTART IDENTITY", TABLE_NAME_TICKETS);

    public JdbcTicketRepository(DataSource pool) {
        this.pool = pool;
    }

    /**
     * Метод осуществляет добавление билета в базу данных. При этом билету присваивается уникальный идентификатор.
     * @param ticket объект билета.
     * @return объект с присвоинным идентификатором если добавление билета произошло успешно,
     * либо Optional.empty(), если место сеанса уже занято.
     */
    @Override
    public Optional<Ticket> add(Ticket ticket) {
        Optional<Ticket> optionalTicket = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(ADD_STATEMENT,
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            ps.setInt(1, ticket.getSessionId());
            ps.setInt(2, ticket.getPosRow());
            ps.setInt(3, ticket.getCell());
            ps.setInt(4, ticket.getUserId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    ticket.setId(id.getInt(1));
                    optionalTicket = Optional.of(ticket);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in JdbcTicketRepository", e);
        }
        return optionalTicket;
    }

    /**
     * Осуществляет поиск билетов по пользователю, забронировавшему их.
     * @param user объект пользователя, для которого требуется найти билеты.
     * @return список билетов если билеты найдены, либо пустой список, если билеты не найдены.
     */
    @Override
    public List<Ticket> findByUser(User user) {
        return find(FIND_BY_USERID_STATEMENT, user.getId());
    }

    /**
     * Осуществляет поиск билетов по идентификатору билета.
     * @param id иденитификатор билета
     * @return возварщает объект билета обернутый в Optional если билет найден,
     * либо Optional.empty() если билет не найден.
     */
    @Override
    public Optional<Ticket> findById(int id) {
        List<Ticket> tickets = find(FIND_BY_SESSIONID_STATEMENT, id);
        if (tickets.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(tickets.get(0));
    }

    /**
     * Метод осуществляет поиск билетов в базе данных.
     * @param statement выражение, описывающее запрос к базе данных для поиска билетов.
     * @param id идентификатор сущности, по которой осуществляется поиск.
     * @return список билетов, удовлетворяющих запросу, либо пустой список.
     */
    private List<Ticket> find(String statement, int id) {
        List<Ticket> tickets = new LinkedList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(statement)
        ) {
            ps.setInt(1, id);
            ps.execute();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(createTicket(rs));
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in JdbcTicketRepository", e);
        }
        return tickets;

    }

    /**
     * Формирует объект Ticket из данных в ResultSet
     * @param rs результат запроса
     * @return объект Ticket
     * @throws SQLException выбрасывается исключение в случае отсутствия необходимых данных в ResultSet
     */
    private Ticket createTicket(ResultSet rs) throws SQLException {
        return new Ticket(
                rs.getInt("id"),
                rs.getInt("session_id"),
                rs.getInt("pos_row"),
                rs.getInt("cell"),
                rs.getInt("user_id")
            );
    }

    /**
     * Удаляет билет из базы
     * @param id идентификатор билета
     * @return удаленный билет обернутый в Optional, либо Optional.empty() если произошла обшибка удаления.
     */
    @Override
    public Optional<Ticket> delete(int id) {
        throw new UnsupportedOperationException("Not implemented, yet");
    }

    /**
     * Метод очищает таблицу Tickets в базе даных
     */
    public void truncateTable() {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(TRUNCATE_TABLE)
        ) {
            ps.execute();
        } catch (Exception e) {
            LOG.error("Exception in JdbcTicketRepository", e);
        }
    }

}
