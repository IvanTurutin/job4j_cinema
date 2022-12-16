package ru.job4j.cinema.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.cinema.Main;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;

import javax.sql.DataSource;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcTicketRepositoryTest {

    private static JdbcTicketRepository ticketStore;
    private static JdbcUserRepository userStore;
    private static JdbcSessionRepository sessionStore;
    private static JdbcHallRepository hallRepository;


    @BeforeAll
    public static void initStore() {
        DataSource pool = new Main().loadPool();
        ticketStore = new JdbcTicketRepository(pool);
        userStore = new JdbcUserRepository(pool);
        sessionStore = new JdbcSessionRepository(pool);
        hallRepository = new JdbcHallRepository(new Main().loadPool());

        ticketStore.truncateTable();
        userStore.truncateTable();
        sessionStore.truncateTable();
        hallRepository.truncateTable();
    }

    @AfterEach
    public void truncateTable() {
        ticketStore.truncateTable();
        userStore.truncateTable();
        sessionStore.truncateTable();
        hallRepository.truncateTable();
    }

    @Test
    void whenAddTicket() {
        User user1 = new User(0, "User1", "123", "1@3.com", "321");
        userStore.add(user1);

        Hall hall = new Hall(0, "first", 5, 10);
        hallRepository.add(hall);

        Session session1 = new Session(0, "Session1", hall.getId());
        sessionStore.add(session1);

        Ticket ticket = new Ticket(
                0,
                session1.getId(),
                2,
                3,
                user1.getId()
        );
        ticketStore.add(ticket);

        Optional<Ticket> ticketInDB = ticketStore.findById(ticket.getId());
        assertThat(ticketInDB.isPresent()).isTrue();
        assertThat(ticketInDB.get().getUserId()).isEqualTo(ticket.getUserId());
    }

    @Test
    void whenFindByUser() {
        User user1 = new User(0, "User1", "123", "1@3.com", "321");
        userStore.add(user1);

        Hall hall = new Hall(0, "first", 5, 10);
        hallRepository.add(hall);

        Session session1 = new Session(0, "Session1", hall.getId());
        sessionStore.add(session1);

        Ticket ticket = new Ticket(
                0,
                session1.getId(),
                2,
                3,
                user1.getId()
        );
        ticketStore.add(ticket);
        Ticket ticket2 = new Ticket(
                0,
                session1.getId(),
                2,
                4,
                user1.getId()
        );
        ticketStore.add(ticket2);

        List<Ticket> ticketsInDB = ticketStore.findByUser(user1);
        assertThat(ticketsInDB).isNotEmpty().hasSize(2).contains(ticket, ticket2);
    }

    @Test
    void whenAddTicketTwice() {
        User user1 = new User(0, "User1", "123", "1@3.com", "321");
        User user2 = new User(0, "User2", "456", "4@6.com", "654");
        userStore.add(user1);
        userStore.add(user2);

        Hall hall = new Hall(0, "first", 5, 10);
        hallRepository.add(hall);

        Session session1 = new Session(0, "Session1", hall.getId());
        sessionStore.add(session1);

        Ticket ticket = new Ticket(
                0,
                session1.getId(),
                2,
                3,
                user1.getId()
        );
        Ticket ticket2 = new Ticket(
                0,
                session1.getId(),
                2,
                3,
                user2.getId()
        );
        ticketStore.add(ticket);
        ticketStore.add(ticket2);

        List<Ticket> ticketsInDB = ticketStore.findByUser(user1);
        assertThat(ticketsInDB).isNotEmpty().contains(ticket);

        List<Ticket> ticketsInDB2 = ticketStore.findByUser(user2);
        assertThat(ticketsInDB2).isEmpty();
    }

}