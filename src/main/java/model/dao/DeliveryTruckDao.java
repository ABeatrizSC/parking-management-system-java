package model.dao;

import model.entities.DeliveryTruck;
import model.entities.MonthlyPayer;

public interface DeliveryTruckDao {
    void insert(DeliveryTruck deliveryTruck);
    DeliveryTruck findByLicensePlate(String licensePlate);
}
