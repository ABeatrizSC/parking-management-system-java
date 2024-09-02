package model.entities;

public class DeliveryTruck {
    private Integer id;
    private String licensePlate;

    private Vehicle vehicle;

    public DeliveryTruck() {
    }

    public DeliveryTruck(Integer id, String licensePlate, Vehicle vehicle) {
        this.id = id;
        this.licensePlate = licensePlate;
        this.vehicle = vehicle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
