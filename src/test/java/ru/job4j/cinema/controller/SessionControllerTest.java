package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.cinema.model.Hall;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.SimpleSessionService;

import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


class SessionControllerTest {

    @Test
    void whenSessionIsPresent() {
        HttpSession session = mock(HttpSession.class);
        SimpleSessionService sessionService = mock(SimpleSessionService.class);
        SessionController sessionController = new SessionController(sessionService);
        Model model = mock(Model.class);

        User user = new User(1, "User1", "123", "1@3.com", "321");
        Session cinemaSession = new Session(
                1,
                "Session 1",
                1
        );
        int id = cinemaSession.getId();

        when(session.getAttribute("user")).thenReturn(user);
        when(sessionService.findById(id)).thenReturn(Optional.of(cinemaSession));

        String page = sessionController.formShowSession(model, session, id);

        verify(model).addAttribute("user", user);
        assertThat(page).isEqualTo("ticket/selectRow");
    }

    @Test
    void whenSessionIsOut() {
        HttpSession session = mock(HttpSession.class);
        SimpleSessionService sessionService = mock(SimpleSessionService.class);
        SessionController sessionController = new SessionController(sessionService);
        Model model = mock(Model.class);

        User user = new User(1, "User1", "123", "1@3.com", "321");
        Session cinemaSession = new Session(
                1,
                "Session 1",
                1
        );
        int id = cinemaSession.getId();

        when(session.getAttribute("user")).thenReturn(user);
        when(sessionService.findById(id)).thenReturn(Optional.empty());

        String page = sessionController.formShowSession(model, session, id);

        verify(model).addAttribute("user", user);

        assertThat(page).isEqualTo("fail");
    }

}