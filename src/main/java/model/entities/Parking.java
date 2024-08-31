package model.entities;

import enums.AccessType;
import enums.VehicleCategory;
import model.dao.DeliveryTruckDao;
import model.dao.MonthlyPayerDao;
import model.dao.VehicleDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static model.dao.DaoFactory.*;

public class Parking {
    public static VehicleCategory chooseCategory(Scanner sc) {
        int chosenCategory = 0;

        while (true) {
            int count = 1;
            for (VehicleCategory vehicle : VehicleCategory.values()) {
                System.out.println("[" + count + "] " + vehicle);
                count++;
            }

            if (sc.hasNextInt()) {
                chosenCategory = sc.nextInt();

                if (chosenCategory >= 1 && chosenCategory <= VehicleCategory.values().length) {
                    break;
                } else {
                    System.out.println("Invalid choice! Please choose a valid number:");
                }
            } else {
                System.out.println("Invalid input! Please enter a number.");
                sc.next();
            }
        }

        return VehicleCategory.values()[chosenCategory - 1];
    }

    public static AccessType chooseAccessType(Scanner sc, VehicleCategory vehicleCategory) {
        int chosenAccessType = 0;
        AccessType[] availableAccessTypes = AccessType.values();
        int[] validChoices = new int[availableAccessTypes.length];
        int validCount = 0;

        while (true) {
            int count = 1;
            validCount = 0;

            for (int i = 0; i < availableAccessTypes.length; i++) {
                AccessType access = availableAccessTypes[i];
                if (access.getCategoriesAvailable().contains(vehicleCategory)) {
                    System.out.println("[" + count + "] " + access);
                    validChoices[count - 1] = i;
                    validCount++;
                    count++;
                }
            }

            if (sc.hasNextInt()) {
                chosenAccessType = sc.nextInt();

                if (chosenAccessType >= 1 && chosenAccessType <= validCount) {
                    break;
                } else {
                    System.out.println("Invalid choice! Please choose a valid access type:");
                }
            } else {
                System.out.println("Invalid input! Please enter a number.");
                sc.next();
            }
        }

        return availableAccessTypes[validChoices[chosenAccessType - 1]];
    }

    public static String captureAValidLicensePlate() {
        Scanner sc = new Scanner(System.in);
        String newLicensePlate;

        while (true) {
            newLicensePlate = sc.next();
            if (newLicensePlate.length() >= 7 && newLicensePlate.length() <= 8) {
                break;
            }
            System.out.print("Please, inform a valid license plate: ");
        }
        return newLicensePlate.toUpperCase();
    }

    public static Vehicle captureMonthlyPayerAccessInfo(Scanner sc, Vehicle vehicle) {
        MonthlyPayerDao monthlyPayerDao = createMonthlyPayerDao();
        VehicleDao vehicleDao = createVehicleDao();

        System.out.println("Type of access: MONTHLY PAYER");
        System.out.println("[1] Log in");
        System.out.println("[2] Register");
        int resMp = sc.nextInt();
        System.out.print("Enter your license plate: ");
        String licensePlate = captureAValidLicensePlate();

        if (resMp == 1) {
            vehicle = logInAMonthlyPayer(monthlyPayerDao, licensePlate, vehicleDao);
        } else {
            vehicle = registerAMonthlyPayer(monthlyPayerDao, licensePlate, vehicle);
        }
        return vehicle;
    }

    public static Vehicle logInAMonthlyPayer(MonthlyPayerDao monthlyPayerDao, String licensePlate, VehicleDao vehicleDao) {
        while (monthlyPayerDao.findByLicensePlate(licensePlate) == null) {
            System.out.print("No monthly member with this license plate was found. Try again: ");
            licensePlate = captureAValidLicensePlate();
        }
        return vehicleDao.findByLicensePlate(licensePlate);
    }

    public static Vehicle registerAMonthlyPayer(MonthlyPayerDao monthlyPayerDao, String licensePlate, Vehicle vehicle) {
        VehicleDao vehicleDao = createVehicleDao();

        if (monthlyPayerDao.findByLicensePlate(licensePlate) != null) {
            System.out.print("The license plate already has a registration. Enter your license plate again to log in: ");
            licensePlate = captureAValidLicensePlate();
            logInAMonthlyPayer(monthlyPayerDao, licensePlate, vehicleDao);
        } else {
            vehicleDao.insert(vehicle);
            MonthlyPayer monthlyPayer = new MonthlyPayer(null, licensePlate, vehicle);
            monthlyPayerDao.insert(monthlyPayer);
        }
        return vehicle;
    }

    public static void captureDeliveryTrucksAccessInfo(Scanner sc, Vehicle vehicle){
        DeliveryTruckDao deliveryTruckDao = createDeliveryTruckDao();
        VehicleDao vehicleDao = createVehicleDao();

        System.out.println("Type of access: DELIVERY TRUCK");
        System.out.println("[1] Log in");
        System.out.println("[2] Register");
        int resMp = sc.nextInt();
        System.out.print("Enter your license plate: ");
        String licensePlate = captureAValidLicensePlate();

        if (resMp == 1) {
            logInADeliveryTruck(deliveryTruckDao, licensePlate, vehicleDao);
        } else {
            registerADeliveryTruck(deliveryTruckDao, licensePlate, vehicle);
        }
    }

    public static Vehicle logInADeliveryTruck(DeliveryTruckDao deliveryTruckDao, String licensePlate, VehicleDao vehicleDao) {
        while (deliveryTruckDao.findByLicensePlate(licensePlate) == null) {
            System.out.print("No monthly member with this license plate was found. Try again: ");
            licensePlate = captureAValidLicensePlate();
        }
        return vehicleDao.findByLicensePlate(licensePlate);
    }

    public static Vehicle registerADeliveryTruck(DeliveryTruckDao deliveryTruckDao, String licensePlate, Vehicle vehicle) {
        VehicleDao vehicleDao = createVehicleDao();

        if (deliveryTruckDao.findByLicensePlate(licensePlate) != null) {
            System.out.print("The license plate already has a registration. Enter your license plate again to log in: ");
            licensePlate = captureAValidLicensePlate();
            logInADeliveryTruck(deliveryTruckDao, licensePlate, vehicleDao);
        } else {
            vehicleDao.insert(vehicle);
            DeliveryTruck deliveryTruck = new DeliveryTruck(null, licensePlate, vehicle);
            deliveryTruckDao.insert(deliveryTruck);
        }
        return vehicle;
    }

    public static Integer chooseAEntranceGate(Scanner sc){
        Integer selectionedGate;

        System.out.println("Choose a gate:");
        List<Integer> availableGates = new ArrayList<>();
        availableGates = Gate.GateType.ENTRANCE.getGateNumbers();
        int count = 1;
        for (Integer gate : availableGates) {
            System.out.println("[" + count + "]" + " CANCELA " + gate);
            count++;
        }

        while(true){
            selectionedGate = sc.nextInt();
            if (selectionedGate >= 1 && selectionedGate <= availableGates.size()) {
                break;
            }
            System.out.println("Invalid gate number. Please, try again:");
        }

        return selectionedGate;
    }

}

