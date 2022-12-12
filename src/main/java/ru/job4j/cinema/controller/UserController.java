package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.SimpleUserService;
import ru.job4j.cinema.util.ControllerUtility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@ThreadSafe
@Controller
public class UserController {
    private final SimpleUserService userService;

    public UserController(SimpleUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/formAddUser")
    public String addUser(Model model, HttpSession session) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        return "user/addUser";
    }

    @PostMapping("/registration")
    public String registration(Model model, @ModelAttribute User user) {
        Optional<User> regUser = userService.add(user);
        if (regUser.isEmpty()) {
            return "redirect:/failRegistration";
        }
        return "redirect:/successRegistration";
    }

    @GetMapping("/failRegistration")
    public String failRegistration(Model model, HttpSession session) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        model.addAttribute("message", "Пользователь с такой почтой или номером телефона уже существует.");
        return "message/fail";
    }

    @GetMapping("/successRegistration")
    public String successRegistration(Model model, HttpSession session) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        model.addAttribute("message", "Пользователь успешно зарегистрирован.");
        return "message/success";
    }

    @GetMapping("/loginPage")
    public String loginPage(Model model, @RequestParam(name = "fail", required = false) Boolean fail, HttpSession session) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        model.addAttribute("fail", fail != null);
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, HttpServletRequest req) {
        Optional<User> userDb = userService.findByEmailAndPassword(
                user.getEmail(), user.getPassword()
        );
        if (userDb.isEmpty()) {
            return "redirect:/loginPage?fail=true";
        }
        HttpSession session = req.getSession();
        session.setAttribute("user", userDb.get());
        return "redirect:/index";
    }

    @GetMapping ("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/index";
    }
}
