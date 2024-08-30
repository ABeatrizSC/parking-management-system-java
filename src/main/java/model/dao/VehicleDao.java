package model.dao;

import model.entities.Vehicle;

public interface VehicleDao {
    void insert(Vehicle vehicle);
    void deleteById(Integer id);
    Vehicle findById(Integer id);
}
