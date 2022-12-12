package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.cinema.service.SimpleSessionService;
import ru.job4j.cinema.util.ControllerUtility;

import javax.servlet.http.HttpSession;

@ThreadSafe
@Controller
public class IndexController {
    private final SimpleSessionService sessionService;

    public IndexController(SimpleSessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/index")
    public String index(Model model, HttpSession session) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        model.addAttribute("sessions", sessionService.findAll());
        return "index";
    }

}
