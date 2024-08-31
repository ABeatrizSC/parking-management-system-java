package model.dao;

import model.entities.Vehicle;

public interface VehicleDao {
    void insert(Vehicle vehicle);
    void updateSelectionedGate(Vehicle vehicle, Integer selectionedGate);
    void deleteById(Integer id);
    Vehicle findById(Integer id);
    Vehicle findByLicensePlate(String licensePLate);
}
