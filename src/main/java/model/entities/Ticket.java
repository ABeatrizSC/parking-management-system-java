package model.entities;

import java.time.Duration;
import java.time.LocalTime;

import static UI.Colors.ANSI_RED;
import static UI.Colors.ANSI_RESET;

public class Ticket {
    private Integer id;
    private LocalTime startHour;
    private LocalTime finishHour;
    private final double BASIC_PAYMENT = 5.00;
    private final double PRICE_PER_MINUTE = 0.10;
    private Double totalValue = 0.00;
    private String parkingSpaces;
    private Vehicle vehicle;

    public Ticket(){}

    public Ticket(Integer id, Vehicle vehicle) {
        this.id = id;
        this.vehicle = vehicle;
    }

    public Ticket(String parkingSpaces, Integer entranceGate, Integer exitGate, Vehicle vehicle, LocalTime startHour, Integer id) {
        this.parkingSpaces = parkingSpaces;
        this.vehicle = vehicle;
        this.startHour = startHour;
        this.id = id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getParkingSpaces() {
        return parkingSpaces;
    }

    public void setParkingSpaces(String parkingSpaces) {
        this.parkingSpaces = parkingSpaces;
    }

    public Double getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(Double totalValue) {
        this.totalValue = totalValue;
    }

    public double getBASIC_PAYMENT() {
        return BASIC_PAYMENT;
    }

    public double getPRICE_PER_MINUTE() {
        return PRICE_PER_MINUTE;
    }

    public LocalTime getFinishHour() {
        return finishHour;
    }

    public void setFinishHour(LocalTime finishHour) {
        this.finishHour = finishHour;
    }

    public LocalTime getStartHour() {
        return startHour;
    }

    public void setStartHour(LocalTime startHour) {
        this.startHour = startHour;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void calculateTotalValue() {
        Duration duration = Duration.between(startHour, finishHour);
        long minutesParked = duration.toMinutes();

        int slotsOccupied = vehicle.getSlotSize();

        double total = minutesParked * PRICE_PER_MINUTE * slotsOccupied;

        if (total <= BASIC_PAYMENT) {
            total = BASIC_PAYMENT;
        }

        this.totalValue = total;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("==============================\n")
                .append("TICKET NUMBER #").append(id).append(":\n")
                .append("Vehicle ID: ").append(vehicle.getId()).append("\n")
                .append("Start Hour: ").append(startHour).append("\n")
                .append("Parking Spaces: ").append(parkingSpaces).append("\n")
                .append("Entrance Gate: ").append(vehicle.getEntranceGate()).append("\n");

        if (finishHour != null && vehicle.getExitGate() != null && totalValue != null) {
            sb.append("Finish Hour: ").append(finishHour).append("\n")
                    .append("Exit Gate: ").append(vehicle.getExitGate()).append("\n")
                    .append("Total Value: ").append(totalValue).append("\n");
        } else {
            sb.append("Finish Hour: -\n")
                    .append("Exit Gate: -\n")
                    .append("Total value: -\n")
                    .append(ANSI_RED + "Warning: You will need to provide your ticket number at the exit.\n" + ANSI_RESET);
        }

        sb.append("==============================");
        return sb.toString();
    }

}
