package model.entities;

import java.time.LocalTime;
import java.util.Arrays;

import static UI.Colors.ANSI_RED;
import static UI.Colors.ANSI_RESET;

public class Ticket {
    private Integer id;
    private LocalTime startHour;
    private LocalTime finishHour;
    private final Double basicPayment = 5.00;
    private Double totalValue;
    private String parkingSpaces;
    private Integer entranceGate;
    private Integer exitGate;
    private Vehicle vehicle;

    public Ticket(){}

    public Ticket(Integer id, Vehicle vehicle) {
        this.id = id;
        this.vehicle = vehicle;
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

    public Double getBasicPayment() {
        return basicPayment;
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

    public Integer getEntranceGate() {
        return entranceGate;
    }

    public void setEntranceGate(Integer entranceGate) {
        this.entranceGate = entranceGate;
    }

    public Integer getExitGate() {
        return exitGate;
    }

    public void setExitGate(Integer exitGate) {
        this.exitGate = exitGate;
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

        if (finishHour != null && exitGate != null && totalValue != null) {
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
