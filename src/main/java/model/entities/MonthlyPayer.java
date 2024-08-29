package model.entities;

public class MonthlyPayer {
    private Integer id;
    private String numberPlate;
    private final Double valuePerMonth = 250.00;

    private Vehicle vehicle;

    public MonthlyPayer(Integer id, String numberPlate) {
        this.id = id;
        this.numberPlate = numberPlate;
    }

    public Integer getId() {
        return id;
    }

    public String getNumberPlate() {
        return numberPlate;
    }

    public Double getValuePerMonth() {
        return valuePerMonth;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }
}
