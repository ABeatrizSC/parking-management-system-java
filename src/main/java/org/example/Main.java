package org.example;

import enums.AccessType;
import enums.VehicleCategory;
import model.dao.VehicleDao;
import model.entities.*;

import java.util.*;

import static model.dao.DaoFactory.*;
import static model.entities.Gate.*;
import static model.entities.Parking.*;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        VehicleCategory vehicleCategory;
        AccessType accessType;
        Vehicle vehicle;
        Ticket ticket = new Ticket();

        VehicleDao vehicleDao = createVehicleDao();

        System.out.println("HELLO, DRIVER!");
        System.out.println("You are:");
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
        if (vehicleCategory != VehicleCategory.PUBLIC_SERVICE && vehicleCategory != VehicleCategory.DELIVERY_TRUCKS) {
            System.out.println("Choose an access type:");
            accessType = chooseAccessType(sc, vehicleCategory);
        } else {
           accessType = AccessType.valueOf(vehicleCategory.toString());
        }

        if (res == 1){
            vehicle = new Vehicle(null, vehicleCategory, accessType);
            switch (accessType){
                case MONTHLY_PAYER:
                    vehicle = captureMonthlyPayerAccessInfo(sc, vehicle);
                    break;
                case DELIVERY_TRUCKS:
                    vehicle = captureDeliveryTrucksAccessInfo(sc, vehicle);
                    break;
                case TICKET:
                    ticket = createTicketAccess(vehicle);
                    break;
                case PUBLIC_SERVICE:
                    vehicle = capturePublicServiceAccessInfo(vehicle);
                    break;
            }

            int[] parkingSpaces = null;
            if (accessType != AccessType.PUBLIC_SERVICE) {
                parkingSpaces = captureValidParkingSpaces(vehicle);
                occupyParkingSpace(parkingSpaces, vehicle);
            }

            operateEntranceGate(vehicle, vehicleDao, sc);

            if (accessType == AccessType.TICKET){
                 updateTicketInformation(vehicle, ticket, parkingSpaces);
                System.out.println(ticket);
            }
        } else {
            Integer exitGate = operateExitGate(sc, vehicleCategory);

            switch (accessType){
                case MONTHLY_PAYER:
                    finalizeMonthlyPayerAccess();
                    break;
                case DELIVERY_TRUCKS:
                    finalizeDeliveryTrucksAccess();
                    break;
                case TICKET:
                    finalizeTicketAccess(sc, exitGate);
                    break;
                case PUBLIC_SERVICE:
                    break;
            }
        }

        sc.close();
    }
}