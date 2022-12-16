package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.SimpleTicketService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketControllerTest {

    @Test
    void ticketIsReserved() {
        SimpleTicketService simpleTicketService = mock(SimpleTicketService.class);
        TicketController ticketController = new TicketController(simpleTicketService);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        User user1 = new User(0, "User1", "123", "1@3.com", "321");
        Session  cinemaSession = new Session(0, "Session1", 3);
        Ticket ticket = new Ticket(0, 0, 2, 3, 0);
        Ticket fullTicket = new Ticket(0, cinemaSession.getId(), 2, 3, user1.getId());

        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("cinemaSession")).thenReturn(cinemaSession);
        when(session.getAttribute("user")).thenReturn(user1);
        when(simpleTicketService.add(ticket)).thenReturn(Optional.of(ticket));

        String page = ticketController.buyTicket(ticket, req);

        verify(simpleTicketService).add(fullTicket);

        assertThat(page).isEqualTo("redirect:successPurchase");
    }

    @Test
    void ticketIsNotReserved() {
        SimpleTicketService simpleTicketService = mock(SimpleTicketService.class);
        TicketController ticketController = new TicketController(simpleTicketService);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        User user1 = new User(0, "User1", "123", "1@3.com", "321");
        Session  cinemaSession = new Session(0, "Session1", 3);
        Ticket ticket = new Ticket(0, 0, 2, 3, 0);
        Ticket fullTicket = new Ticket(0, cinemaSession.getId(), 2, 3, user1.getId());

        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("cinemaSession")).thenReturn(cinemaSession);
        when(session.getAttribute("user")).thenReturn(user1);
        when(simpleTicketService.add(ticket)).thenReturn(Optional.empty());

        String page = ticketController.buyTicket(ticket, req);

        verify(simpleTicketService).add(fullTicket);

        assertThat(page).isEqualTo("redirect:seatIsTaken");
    }
}