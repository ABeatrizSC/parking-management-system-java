package model.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Ticket {
    private Integer id;
    private Date startHour;
    private Date finishHour;
    private double basicPayment;
    private double totalValue;
    //vaga ocupada
    private Set<ParkingSpace> parkingSpaces = new HashSet<ParkingSpace>();
    //cancela que entrou e saiu
    private Vehicle vehicle;
}
