package ru.job4j.cinema.model;

import java.util.Objects;

/**
 * Модель данных сеанс
 */
public class Session {
    /**
     * Идентификатор сеанса
     */
    private int id;
    /**
     * Название сеанса
     */
    private String name;
    /**
     * Идентификатор зала, в котором проходит сеанс
     */
    private int hallId;

    public Session() {
    }

    public Session(int id, String name, int hallId) {
        this.id = id;
        this.name = name;
        this.hallId = hallId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHallId() {
        return hallId;
    }

    public void setHallId(int hallId) {
        this.hallId = hallId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Session session = (Session) o;
        return id == session.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Session{"
                + "id=" + id
                + ", name='" + name + '\''
                + ", hall='" + hallId + '\''
                + '}';
    }
}
