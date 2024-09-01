package model.entities;

import enums.SlotType;

public class ParkingSpace {
    private Integer id;
    private SlotType type;
    private Boolean isOccupied;

    Vehicle vehicle;

    public ParkingSpace() {
    }

    public ParkingSpace(Integer id, SlotType type, Boolean isOccupied, Vehicle vehicle) {
        this.id = id;
        this.type = type;
        this.isOccupied = isOccupied;
        this.vehicle = vehicle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public SlotType getType() {
        return type;
    }

    public void setType(SlotType type) {
        this.type = type;
    }

    public Boolean getIsOccupied() {
        return isOccupied;
    }

    public void setIsOccupied(Boolean isOccupied) {
        this.isOccupied = isOccupied;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
