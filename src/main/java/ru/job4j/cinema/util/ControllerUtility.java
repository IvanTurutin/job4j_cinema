package ru.job4j.cinema.util;

import ru.job4j.cinema.model.User;

import javax.servlet.http.HttpSession;

public final class ControllerUtility {
    private ControllerUtility() {

    }

    private static final String GUEST = "Гость";

    public static User checkUser(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = new User();
            user.setEmail(GUEST);
        }
        return user;
    }
}
