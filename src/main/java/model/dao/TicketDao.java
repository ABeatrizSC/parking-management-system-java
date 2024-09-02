package model.dao;

import model.entities.Ticket;
import model.entities.Vehicle;

public interface TicketDao {
    void insert(Ticket ticket);
    Ticket findById(Integer id);
    void deleteById(Integer id);
    void updateEntranceInformation(Vehicle vehicle, Ticket ticket, int[] parkingSpaces);
    void updateExitInformation(Ticket ticket);
}
