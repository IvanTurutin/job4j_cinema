package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.service.SimpleSessionService;
import ru.job4j.cinema.util.ControllerUtility;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@ThreadSafe
@Controller
public class SessionController {
    private final SimpleSessionService sessionService;

    public SessionController(SimpleSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/formShowSession/{sessionId}")
    public String formShowSession(Model model, HttpSession session, @PathVariable("sessionId") int id) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        Optional<Session> cinemaSession = sessionService.findById(id);
        if (cinemaSession.isEmpty()) {
            model.addAttribute("message", "Такой сеанс не найден.");
            return "fail";
        }
        session.setAttribute("cinemaSession", cinemaSession.get());
        return "ticket/selectRow";
    }

    @GetMapping("/failSession")
    public String failSession(Model model, HttpSession session) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        model.addAttribute("message", "Такой фильм не найден.");
        return "message/fail";
    }

}
