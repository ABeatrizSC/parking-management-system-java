package model.dao.impl;

import model.dao.TicketDao;
import model.entities.Ticket;

import java.sql.Connection;

public class TicketDaoJDBC implements TicketDao {
    private Connection conn;

    public TicketDaoJDBC(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void insert(Ticket ticket) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public void updateTime() {

    }
}
