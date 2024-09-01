package model.dao;

import model.entities.DeliveryTruck;

public interface DeliveryTruckDao {
    void insert(DeliveryTruck deliveryTruck);
    DeliveryTruck findDeliveryTruckByLicensePlate(String licensePlate);
}
