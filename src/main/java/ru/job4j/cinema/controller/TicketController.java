package ru.job4j.cinema.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.cinema.model.Session;
import ru.job4j.cinema.model.Ticket;
import ru.job4j.cinema.model.User;
import ru.job4j.cinema.service.TicketService;
import ru.job4j.cinema.util.ControllerUtility;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@ThreadSafe
@Controller
public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping("/prepareSelectCell")
    public String prepareSelectCell(HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.setAttribute("posRow", req.getParameter("posRow"));
        return "redirect:selectCell";
    }

    @GetMapping("/selectCell")
    public String selectCell(Model model, HttpSession session) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        return "ticket/selectCell";
    }

    @PostMapping("/preparePurchaseConfirmation")
    public String purchaseConfirmation(HttpServletRequest req) {
        HttpSession session = req.getSession();
        session.setAttribute("cell", req.getParameter("cell"));
        return "redirect:purchaseConfirmation";
    }

    @GetMapping("/purchaseConfirmation")
    public String purchaseConfirmation(Model model, HttpSession session) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        return "ticket/purchaseConfirmation";
    }

    @PostMapping("/buyTicket")
    public String buyTicket(@ModelAttribute Ticket ticket, HttpServletRequest req) {
        HttpSession session = req.getSession();
        Session cinemaSession = (Session) session.getAttribute("cinemaSession");
        User user = (User) session.getAttribute("user");
        ticket.setSessionId(cinemaSession.getId());
        ticket.setUserId(user.getId());
        System.out.println("ticket in buyTicket:" + ticket);
        if (ticketService.add(ticket).isEmpty()) {
            return "redirect:seatIsTaken";
        }
        return "redirect:successPurchase";
    }

    @GetMapping("/seatIsTaken")
    public String seatIsTaken(Model model, HttpSession session) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        model.addAttribute("message", "Билет на это место уже забронирован. Выберите другое место");
        return "message/fail";
    }

    @GetMapping("/successPurchase")
    public String successPurchase(Model model, HttpSession session) {
        model.addAttribute("user", ControllerUtility.checkUser(session));
        model.addAttribute("message", "Билет забронирован.");
        return "message/success";
    }

}
