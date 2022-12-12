package ru.job4j.cinema.repository;

import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * Хранилище билетов
 * @see ru.job4j.cinema.model.Ticket
 */
@ThreadSafe
@Repository
public class PostgresTicketRepository implements TicketRepository {
    private final DataSource pool;
    private static final Logger LOG = LoggerFactory.getLogger(PostgresSessionRepository.class.getName());

    private static final String TABLE_NAME_TICKETS = "tickets";
    private static final String TABLE_NAME_USERS = "users";
    private static final String TABLE_NAME_SESSIONS = "sessions";
    private static final String TABLE_NAME_HALLS = "halls";
    private static final String ADD_STATEMENT = String.format(
            "INSERT INTO %s(session_id, pos_row, cell, user_id) VALUES (?, ?, ?, ?)",
            TABLE_NAME_TICKETS);
    private static final String SELECT_TICKETS_STATEMENT = String.format(
            "SELECT t.*, "
                    + "s.name as s_name, "
                    + "s.hall_id as h_id, "
                    + "h.name as h_name, "
                    + "h.rows as h_rows, "
                    + "h.cells as h_cells, "
                    + "u.username as u_name, "
                    + "u.password as u_pass, "
                    + "u.email as u_email, "
                    + "u.phone as u_phone "
                    + "FROM %s as t "
                    + "JOIN %s as s ON t.session_id = s.id "
                    + "JOIN %s as h ON s.hall_id = h.id "
                    + "JOIN %s as u ON t.user_id = u.id ",
            TABLE_NAME_TICKETS,
            TABLE_NAME_SESSIONS,
            TABLE_NAME_HALLS,
            TABLE_NAME_USERS);
    private static final String FIND_BY_USERID_STATEMENT = SELECT_TICKETS_STATEMENT + "WHERE t.user_id = ?";
    private static final String FIND_BY_SESSIONID_STATEMENT = SELECT_TICKETS_STATEMENT + "WHERE t.id = ?";
    private static final String TRUNCATE_TABLE =
            String.format("TRUNCATE TABLE %s RESTART IDENTITY", TABLE_NAME_TICKETS);

    public PostgresTicketRepository(DataSource pool) {
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
            ps.setInt(1, ticket.getCinemaSession().getId());
            ps.setInt(2, ticket.getPosRow());
            ps.setInt(3, ticket.getCell());
            ps.setInt(4, ticket.getUser().getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    ticket.setId(id.getInt(1));
                    optionalTicket = Optional.of(ticket);
                }
            }
        } catch (Exception e) {
            LOG.error("Exception in PostgresTicketRepository", e);
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
        //Optional<Ticket> optionalTicket = Optional.empty();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(statement)
        ) {
            ps.setInt(1, id);
            ps.execute();
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tickets.add(createTicket(rs));
                }
 /*               if (rs.next()) {
                    optionalTicket = Optional.of(createTicket(rs));
                }*/
            }
        } catch (Exception e) {
            LOG.error("Exception in PostgresTicketRepository", e);
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
                createSession(rs),
                rs.getInt("pos_row"),
                rs.getInt("cell"),
                createUser(rs)
            );
    }

    /**
     * Формирует объект Session из данных в ResultSet
     * @param rs результат запроса
     * @return объект Session
     * @throws SQLException выбрасывается исключение в случае отсутствия необходимых данных в ResultSet
     */
    private Session createSession(ResultSet rs) throws SQLException {
        return new Session(
                rs.getInt("session_id"),
                rs.getString("s_name"),
                createHall(rs)
        );
    }

    /**
     * Формирует объект Hall из данных в ResultSet
     * @param rs результат запроса
     * @return объект Hall
     * @throws SQLException выбрасывается исключение в случае отсутствия необходимых данных в ResultSet
     */
    private Hall createHall(ResultSet rs) throws SQLException {
        return new Hall(rs.getInt("h_id"),
                rs.getString("h_name"),
                rs.getInt("h_rows"),
                rs.getInt("h_cells")
        );
    }

    /**
     * Формирует объект User из данных в ResultSet
     * @param rs результат запроса
     * @return объект User
     * @throws SQLException выбрасывается исключение в случае отсутствия необходимых данных в ResultSet
     */
    private User createUser(ResultSet rs) throws SQLException {
        return new User(
                rs.getInt("user_id"),
                rs.getString("u_name"),
                rs.getString("u_pass"),
                rs.getString("u_email"),
                rs.getString("u_phone")
        );
    }

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
            LOG.error("Exception in PostgresTicketRepository", e);
        }
    }

}
