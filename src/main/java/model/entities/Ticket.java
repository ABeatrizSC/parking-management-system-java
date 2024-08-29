package model.entities;

import java.util.Date;

public class Ticket {
    private Integer id;
    private Date startHour;
    private Date finishHour;
    private double basicPayment;
    private double totalValue;

    //vaga ocupada
    private ParkingSpace parkingSpace;
    //cancela que entrou e saiu
    private Vehicle vehicle;
}
