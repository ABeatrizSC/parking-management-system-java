package model.dao;

import model.entities.ParkingSpace;
import model.entities.Vehicle;

public interface ParkingSpaceDao {
    void update(ParkingSpace parkingSpace);
    void insert(ParkingSpace parkingSpace);
    ParkingSpace findById(Integer id);
    int[] findByVehicle(Integer id);

}
