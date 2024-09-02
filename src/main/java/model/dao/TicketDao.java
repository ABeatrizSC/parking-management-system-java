package model.dao;

import model.entities.Ticket;
import model.entities.Vehicle;

public interface TicketDao {
    void insert(Ticket ticket);
    void deleteById(Integer id);
    void updateEntranceInformation(Vehicle vehicle, Ticket ticket, int[] parkingSpaces);
    void updateExitInformation(Vehicle vehicle, Ticket ticket);
}
