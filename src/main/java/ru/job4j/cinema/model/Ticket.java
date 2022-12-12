package ru.job4j.cinema.model;

import java.util.Objects;

/**
 * Модель данных билет
 */
public class Ticket {
    /**
     * Идентификатор билета
     */
    private int id;
    /**
     * Сеанс на который куплен билет
     * @see ru.job4j.cinema.model.Session
     */
    private Session cinemaSession;
    /**
     * Ряд, в котором находится место
     */
    private int posRow;
    /**
     * Место
     */
    private int cell;
    /**
     * Пользователь, купивший билет
     * @see ru.job4j.cinema.model.User
     */
    private User user;

    public Ticket() {
    }

    public Ticket(int id, Session cinemaSession, int posRow, int cell, User user) {
        this.id = id;
        this.cinemaSession = cinemaSession;
        this.posRow = posRow;
        this.cell = cell;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Session getCinemaSession() {
        return cinemaSession;
    }

    public void setCinemaSession(Session cinemaSession) {
        this.cinemaSession = cinemaSession;
    }

    public int getPosRow() {
        return posRow;
    }

    public void setPosRow(int posRow) {
        this.posRow = posRow;
    }

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Ticket ticket = (Ticket) o;
        return id == ticket.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Ticket{"
                + "id=" + id
                + ", session=" + cinemaSession
                + ", posRow=" + posRow
                + ", cell=" + cell
                + ", user=" + user
                + '}';
    }
}
