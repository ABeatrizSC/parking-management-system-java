package model.dao;

import model.entities.Ticket;

public interface TicketDao {
    void insert(Ticket ticket);
    void deleteById(Integer id);
    void updateTime();
}
