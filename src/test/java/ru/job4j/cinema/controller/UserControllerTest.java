package ru.job4j.cinema.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.SimpleUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.*;


import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

class UserControllerTest {

    @Test
    void registrationSuccess() {
        SimpleUserService userService = mock(SimpleUserService.class);
        UserController userController = new UserController(userService);
        Model model = mock(Model.class);
        User user = new User(0, "User1", "123", "1@3.com", "321");

        when(userService.add(user)).thenReturn(Optional.of(user));
        String page = userController.registration(model, user);

        assertThat(page).isEqualTo("redirect:/successRegistration");
    }

    @Test
    void registrationFail() {
        SimpleUserService userService = mock(SimpleUserService.class);
        UserController userController = new UserController(userService);
        Model model = mock(Model.class);
        User user = new User(0, "User1", "123", "1@3.com", "321");

        when(userService.add(user)).thenReturn(Optional.empty());
        String page = userController.registration(model, user);

        assertThat(page).isEqualTo("redirect:/failRegistration");
    }

    @Test
    void loginSuccess() {
        SimpleUserService userService = mock(SimpleUserService.class);
        UserController userController = new UserController(userService);
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);

        User user = new User(0, "User1", "123", "1@3.com", "321");
        User userDb = new User(1, "User1", "123", "1@3.com", "321");

        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.of(userDb));
        when(req.getSession()).thenReturn(session);

        String page = userController.login(user, req);

        verify(session).setAttribute("user", userDb);
        assertThat(page).isEqualTo("redirect:/index");
    }

    @Test
    void loginFail() {
        SimpleUserService userService = mock(SimpleUserService.class);
        UserController userController = new UserController(userService);
        HttpServletRequest req = mock(HttpServletRequest.class);

        User user = new User(0, "User1", "123", "1@3.com", "321");

        when(userService.findByEmailAndPassword(user.getEmail(), user.getPassword())).thenReturn(Optional.empty());

        String page = userController.login(user, req);

        assertThat(page).isEqualTo("redirect:/loginPage?fail=true");
    }

}