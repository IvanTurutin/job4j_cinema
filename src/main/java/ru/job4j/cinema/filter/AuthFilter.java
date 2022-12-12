package ru.job4j.cinema.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

/**
 * Фильтр доступа неавторизованнх пользователей
 */
@Component
public class AuthFilter implements Filter {

    private final static Set<String> ALLOWABLE_URI = Set.of(
            "index",
            "loginPage",
            "login",
            "formAddUser",
            "registration",
            "fail",
            "success",
            "failRegistration",
            "successRegistration"
    );

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        if (checkAllowableUri(uri)) {
            chain.doFilter(req, res);
            return;
        }
        if (req.getSession().getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/loginPage");
            return;
        }
        chain.doFilter(req, res);
    }

    private boolean checkAllowableUri(String uri) {
        return ALLOWABLE_URI.stream().anyMatch(uri::endsWith);
    }
}
