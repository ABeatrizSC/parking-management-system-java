package org.example;

import enums.AccessType;
import enums.VehicleCategory;
import model.dao.VehicleDao;
import model.entities.Vehicle;

import java.util.Scanner;

import static model.dao.DaoFactory.createVehicleDao;
import static model.entities.Parking.chooseAccessType;
import static model.entities.Parking.chooseCategory;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("HELLO, DRIVER!");
        System.out.println("Please, choose your vehicle category:");
        VehicleCategory vehicleCategory = chooseCategory(sc);
        System.out.println("Choose an access type:");
        AccessType accessType = chooseAccessType(sc, vehicleCategory);

        VehicleDao vehicleDao = createVehicleDao();
        Vehicle vehicle = new Vehicle(null, vehicleCategory, accessType);
        vehicleDao.insert(vehicle);

        sc.close();
    }
}