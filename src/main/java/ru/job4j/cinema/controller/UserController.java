package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.UserService;
import ru.job4j.cinema.util.ControllerUtility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@ThreadSafe
@Controller
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Принимает запрос на отображение вида добавления пользователя
     * @param model модель вида
     * @param session сессия подключения
     * @return назнвание шаблона вида добавления пользователя
     */
    @GetMapping("/formAddUser")
    public String addUser(Model model, HttpSession session) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        return "user/addUser";
    }

    /**
     * Производит добавление пользователя в систему
     * @param user сформированный объект на основе введенных пользователем данных
     * @return перенаправляет на страницу /successRegistration при успешной регистрации,
     * и на страницу /failRegistration при неудачной регистрации
     */
    @PostMapping("/registration")
    public String registration(@ModelAttribute User user) {
        Optional<User> regUser = userService.add(user);
        if (regUser.isEmpty()) {
            return "redirect:/failRegistration";
        }
        return "redirect:/successRegistration";
    }

    /**
     * Принимает запрос на отображение вида с ошибкой регистрации пользователя
     * @param model модель вида
     * @param session сессия подключения
     * @return название шаблона для отображения ошибки регистрации
     */
    @GetMapping("/failRegistration")
    public String failRegistration(Model model, HttpSession session) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        model.addAttribute("message", "Пользователь с такой почтой или номером телефона уже существует.");
        return "message/fail";
    }

    /**
     * Принимает запрос на отображение вида с сообщением об успешной регистрации пользователя
     * @param model модель вида
     * @param session сессия подключения
     * @return название шаблона для отображения сообщения об успехе
     */
    @GetMapping("/successRegistration")
    public String successRegistration(Model model, HttpSession session) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        model.addAttribute("message", "Пользователь успешно зарегистрирован.");
        return "message/success";
    }

    /**
     * Принимает запрос на отображение вида авторизации пользователя
     * @param model модель вида
     * @param fail Параметр отвечающий за проверку верности введенного логина и пароля, (false - по умолчанию,
     *             true - отображается сообщение о неверно введенном логине или пароле)
     * @param session сессия подключения
     * @return название шаблона для авторизации пользователя
     */
    @GetMapping("/loginPage")
    public String loginPage(Model model, @RequestParam(name = "fail", required = false) Boolean fail, HttpSession session) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        model.addAttribute("fail", fail != null);
        return "user/login";
    }

    /**
     * Принимает запрос и проводит аутентификацию пользователя
     * @param user сфоримрованный объект User на основе введенных данных пользователем
     * @param req запрос сформированный браузером
     * @return В случае успешной аутентификации предоставляет права пользования сайтом и перенаправляет на вид
     * главной страницы, в случае провала аутентификации переводит параметр fail в значение true и перенаправляет на
     * вид авторизации с отображением сообщения о неверно введенными данными пользователя
     */
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

    /**
     * Принимает запрос на выход пользователя из системы. Очищает сессию от всех атрибутов
     * @param session сессия подключения
     * @return перенаправляет на главную страницу
     */
    @GetMapping ("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/index";
    }
}
