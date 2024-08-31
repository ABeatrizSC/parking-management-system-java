package model.entities;

import java.util.Date;
import java.util.Set;

public class Ticket {
    private Integer id;
    private Date startHour;
    private Date finishHour;
    private double basicPayment;
    private double totalValue;
    private Set<ParkingSpace> parkingSpaces;
    private Vehicle vehicle;
}
