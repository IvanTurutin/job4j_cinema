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

class PostgresTicketRepositoryTest {

    private static PostgresTicketRepository ticketStore;
    private static PostgresUserRepository userStore;
    private static PostgresSessionRepository sessionStore;

    @BeforeAll
    public static void initStore() {
        DataSource pool = new Main().loadPool();
        ticketStore = new PostgresTicketRepository(pool);
        userStore = new PostgresUserRepository(pool);
        sessionStore = new PostgresSessionRepository(pool);
        ticketStore.truncateTable();
        userStore.truncateTable();
        sessionStore.truncateTable();
    }

    @AfterEach
    public void truncateTable() {
        ticketStore.truncateTable();
        userStore.truncateTable();
        sessionStore.truncateTable();
    }

    @Test
    void whenAddTicket() {
        User user1 = new User(0, "User1", "123", "1@3.com", "321");
        userStore.add(user1);

        Session session1 = new Session(0, "Session1", new Hall(3, "", 0, 0));
        sessionStore.add(session1);

        Ticket ticket = new Ticket(
                0,
                session1,
                2,
                3,
                user1
        );
        ticketStore.add(ticket);

        Optional<Ticket> ticketInDB = ticketStore.findById(ticket.getId());
        assertThat(ticketInDB.isPresent()).isTrue();
        assertThat(ticketInDB.get().getUser().getUsername()).isEqualTo(ticket.getUser().getUsername());
    }

    @Test
    void whenFindByUser() {
        User user1 = new User(0, "User1", "123", "1@3.com", "321");
        userStore.add(user1);

        Session session1 = new Session(0, "Session1", new Hall(3, "", 0, 0));
        sessionStore.add(session1);

        Ticket ticket = new Ticket(
                0,
                session1,
                2,
                3,
                user1
        );
        ticketStore.add(ticket);
        Ticket ticket2 = new Ticket(
                0,
                session1,
                2,
                4,
                user1
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

        Session session1 = new Session(0, "Session1", new Hall(3, "", 0, 0));
        sessionStore.add(session1);

        Ticket ticket = new Ticket(
                0,
                session1,
                2,
                3,
                user1
        );
        Ticket ticket2 = new Ticket(
                0,
                session1,
                2,
                3,
                user2
        );
        ticketStore.add(ticket);
        ticketStore.add(ticket2);

        List<Ticket> ticketsInDB = ticketStore.findByUser(ticket.getUser());
        assertThat(ticketsInDB).isNotEmpty().contains(ticket);

        List<Ticket> ticketsInDB2 = ticketStore.findByUser(ticket2.getUser());
        assertThat(ticketsInDB2).isEmpty();
    }

}