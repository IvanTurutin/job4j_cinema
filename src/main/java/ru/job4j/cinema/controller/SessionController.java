package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.service.SessionService;
import ru.job4j.cinema.util.ControllerUtility;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@ThreadSafe
@Controller
public class SessionController {
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    /**
     * Принимает запрос на отображение вида выбора ряда выбранного сеанса
     * @param model модель вида
     * @param session сессия подключения
     * @param id идентификатор сеанса
     * @return назнвание шаблона вида выбора ряда если такой сеанс есть,
     * либо название шаблона ошибки, если сеанс не найден.
     */
    @GetMapping("/formShowSession/{sessionId}")
    public String formShowSession(Model model, HttpSession session, @PathVariable("sessionId") int id) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        Optional<Session> cinemaSession = sessionService.findById(id);
        if (cinemaSession.isEmpty()) {
            model.addAttribute("message", "Такой сеанс не найден.");
            return "message/fail";
        }
        session.setAttribute("cinemaSession", cinemaSession.get());
        return "ticket/selectRow";
    }

}
