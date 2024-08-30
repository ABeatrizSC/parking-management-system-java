package org.example;

import enums.AccessType;
import enums.VehicleCategory;
import model.dao.VehicleDao;
import model.entities.Vehicle;

import java.util.Scanner;

import static model.dao.DaoFactory.createVehicleDao;
import static model.entities.Parking.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        VehicleCategory vehicleCategory;
        AccessType accessType;
        Vehicle vehicle;

        VehicleDao vehicleDao = createVehicleDao();

        System.out.println("HELLO, DRIVER!");
        System.out.println("Please, choose what you want:");
        System.out.println("[1] Entering");
        System.out.println("[2] Exiting");
        int res;
        while(true){
            res = sc.nextInt();
            if (res == 1 || res == 2) {
                break;
            }
            System.out.println("Invalid option. Please choose [1] Entering or [2] Exiting:");
        }

        System.out.println("Choose your vehicle category:");
        vehicleCategory = chooseCategory(sc);
        System.out.println("Choose an access type:");
        accessType = chooseAccessType(sc, vehicleCategory);

        if (res == 1){
            vehicle = new Vehicle(null, vehicleCategory, accessType);

            switch (accessType){
                case MONTHLY_PLAYER:
                    captureMonthlyPayerAccessInfo(sc, vehicle);
                case TICKET:
                case DELIVERY_TRUCKS:
                case PUBLIC_SERVICE:
            }

        } else {
            //exit implementation
        }



        sc.close();
    }
}