package service;

import enums.AccessType;
import enums.SlotType;
import enums.VehicleCategory;
import model.dao.DeliveryTruckDao;
import model.dao.MonthlyPayerDao;
import model.dao.ParkingSpaceDao;
import model.dao.TicketDao;
import model.dao.VehicleDao;
import model.entities.DeliveryTruck;
import model.entities.Gate;
import model.entities.MonthlyPayer;
import model.entities.ParkingSpace;
import model.entities.Ticket;
import model.entities.Vehicle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import static UI.Colors.ANSI_BLUE;
import static UI.Colors.ANSI_RED;
import static UI.Colors.ANSI_RESET;
import static UI.Colors.ANSI_YELLOW;
import static model.dao.DaoFactory.createDeliveryTruckDao;
import static model.dao.DaoFactory.createMonthlyPayerDao;
import static model.dao.DaoFactory.createParkingSpaceDao;
import static model.dao.DaoFactory.createTicketDao;
import static model.dao.DaoFactory.createVehicleDao;

public class ParkingService {
    public static void printParkingSpaces(ParkingSpaceDao parkingSpaceDao) {
        for (int i = 1; i <= 500; i++) {
            String formattedNumber = String.format("%3d", i);

            if (i <= 200) {
                if (parkingSpaceDao.findById(i).getIsOccupied()) {
                    System.out.print(ANSI_RED + formattedNumber + ANSI_RESET + " ");
                } else {
                    System.out.print(ANSI_YELLOW + formattedNumber + ANSI_RESET + " ");
                }
            } else {
                if (parkingSpaceDao.findById(i).getIsOccupied()) {
                    System.out.print(ANSI_RED + formattedNumber + ANSI_RESET + " ");
                } else {
                    System.out.print(ANSI_BLUE + formattedNumber + ANSI_RESET + " ");
                }
            }

            if (i % 30 == 0) {
                System.out.println();
            }
        }
    }
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

    public static Integer chooseAEntranceGate(Scanner sc){
        Integer selectionedGate;

        System.out.println("Choose a gate:");
        List<Integer> availableGates = new ArrayList<>();
        availableGates = Gate.GateType.ENTRANCE.getGateNumbers();
        int count = 1;
        for (Integer gate : availableGates) {
            System.out.println("[" + count + "]" + " GATE " + gate);
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

    public static Integer chooseAExitGate(Scanner sc){
        Integer selectionedGate;

        System.out.println("Choose a gate:");
        List<Integer> availableGates = new ArrayList<>();
        availableGates = Gate.GateType.EXIT.getGateNumbers();
        int count = 1;
        for (Integer gate : availableGates) {
            System.out.println("[" + count + "]" + " GATE " + gate);
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

    public static int[] captureValidParkingSpaces(Vehicle vehicle) {
        ParkingSpaceDao parkingSpaceDao = createParkingSpaceDao();
        Scanner sc = new Scanner(System.in);
        String parkingSpaces;
        int[] spaces;

        System.out.println("Choose parking spaces:");

        printParkingSpaces(parkingSpaceDao);

        System.out.println(" ");
        System.out.println(ANSI_RED+ "WARNINGS:" + ANSI_RESET +
                "\n- The number of parking spaces must be separated by blank spaces.\n- Yellow parking spaces are reserved for monthly payers (1 to 200).\n- Red parking spaces are occupied.");

        while (true) {
            parkingSpaces = sc.nextLine();
            if (Objects.equals(parkingSpaces, "0")){
                System.exit(1);
            }
            String[] p = parkingSpaces.split(" ");
            spaces = new int[p.length];

            if (areSpacesANumber(spaces, p)) {
                for (int i = 0; i < p.length; i++) {
                    spaces[i] = Integer.parseInt(p[i]);
                }
            }

            if (areSpacesValidByAccessType(spaces, vehicle) && areSpacesANumber(spaces, p) &&
                    isCorrectNumberOfSpaces(spaces, vehicle) &&
                    areSpacesSequential(spaces) &&
                    !areAnySpacesOccupied(spaces, parkingSpaceDao)) {
                break;
            }
        }
        return spaces;
    }

    private static Boolean areSpacesValidByAccessType(int[] spaces, Vehicle vehicle) {
        if (vehicle.getAccessType() == AccessType.MONTHLY_PAYER) {
            for (int space : spaces){
                if (space < 1 || space > SlotType.MONTHLY.getQuantity()) {
                    System.out.println("These parking spaces are exclusive to monthly members or unavailable. \nIf there are not enough spaces, enter 0 to exit.");
                    return false;
                }
            }
        } else {
            for (int space : spaces){
                if (space < 201 || space > SlotType.CASUAL.getQuantity() + SlotType.MONTHLY.getQuantity()) {
                    System.out.println("These parking spaces are exclusive to monthly members or don't exist. \nIf there are not enough spaces, enter 0 to exit.");
                    return false;
                }
            }
        }
        return true;
    }

    private static Boolean areSpacesANumber(int[] spaces, String[] p) {
        try {
            for (int i = 0; i < p.length; i++) {
                spaces[i] = Integer.parseInt(p[i]);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Please enter valid numbers for parking spaces. \nIf there are not enough spaces, enter 0 to exit.");
            return false;
        }
        return true;
    }

    private static Boolean isCorrectNumberOfSpaces(int[] spaces, Vehicle vehicle) {
        if (spaces.length != vehicle.getSlotSize()) {
            System.out.println("Error: Incorrect number of parking spaces. Expected: " + vehicle.getSlotSize() + ". \nIf there are not enough spaces, enter 0 to exit.");
            return false;
        }
        return true;
    }

    private static Boolean areAnySpacesOccupied(int[] spaces, ParkingSpaceDao parkingSpaceDao) {
        for (int i = 0; i < spaces.length; i++) {
            ParkingSpace parkingSpace = parkingSpaceDao.findById(spaces[i]);
            if (parkingSpace != null && parkingSpace.getIsOccupied()) {
                System.out.println("Error: Parking Space " + spaces[i] + " is already occupied. \nIf there are not enough spaces, enter 0 to exit.");
                return true;
            }
        }
        return false;
    }

    private static Boolean areSpacesSequential(int[] spaces) {
        Arrays.sort(spaces);
        for (int i = 1; i < spaces.length; i++) {
            if (spaces[i] != spaces[i - 1] + 1) {
                System.out.println("Error: Parking spaces are not sequential. \nCheck if there is the number of sequential parking spaces required for your vehicle and try again:\n(If there are none, enter 0 to exit)");
                return false;
            }
        }
        return true;
    }

    public static void occupyParkingSpace(int[] parkingSpaces, Vehicle vehicle){
        ParkingSpaceDao parkingSpaceDao = createParkingSpaceDao();
        ParkingSpace parkingSpace;
        for (int id : parkingSpaces) {
            if(vehicle.getAccessType() == AccessType.MONTHLY_PAYER) {
                parkingSpace = new ParkingSpace(id, SlotType.MONTHLY, true, vehicle);
            } else {
                parkingSpace = new ParkingSpace(id, SlotType.CASUAL, true, vehicle);
            }
            parkingSpaceDao.update(parkingSpace);
        }
    }

    public static void emptyParkingSpace(Vehicle vehicle){
        ParkingSpaceDao parkingSpaceDao = createParkingSpaceDao();
        ParkingSpace parkingSpace;
        int[] parkingSpaces = parkingSpaceDao.findByVehicle(vehicle.getId());
        for (int id : parkingSpaces) {
            if(vehicle.getAccessType() == AccessType.MONTHLY_PAYER) {
                parkingSpace = new ParkingSpace(id, SlotType.MONTHLY, false, null);
            } else {
                parkingSpace = new ParkingSpace(id, SlotType.CASUAL, false, null);
            }
            parkingSpaceDao.update(parkingSpace);
        }
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
        while (monthlyPayerDao.findMonthlyPayerByLicensePlate(licensePlate) == null) {
            System.out.print("No monthly member with this license plate was found. Try again: ");
            licensePlate = captureAValidLicensePlate();
        }
        MonthlyPayer monthlyPayer = monthlyPayerDao.findMonthlyPayerByLicensePlate(licensePlate);
        return vehicleDao.findById(monthlyPayer.getVehicle().getId());
    }

    public static Vehicle registerAMonthlyPayer(MonthlyPayerDao monthlyPayerDao, String licensePlate, Vehicle vehicle) {
        VehicleDao vehicleDao = createVehicleDao();

        if (vehicleDao.findByLicensePlate(licensePlate) != null) {
            if (vehicleDao.findByLicensePlate(licensePlate).getAccessType() == AccessType.DELIVERY_TRUCKS) {
                System.out.println("This license plate has already been registered in another category.");
                System.out.println("Program execution will terminate. Try again.");
                System.exit(1);
            } else {
                System.out.print("The license plate already has a registration. Enter your license plate again to log in: ");
                licensePlate = captureAValidLicensePlate();
                vehicle = logInAMonthlyPayer(monthlyPayerDao, licensePlate, vehicleDao);
            }
        } else {
            vehicleDao.insert(vehicle);
            MonthlyPayer monthlyPayer = new MonthlyPayer(null, licensePlate, vehicle);
            monthlyPayerDao.insert(monthlyPayer);
        }
        return vehicle;
    }

    public static Vehicle captureDeliveryTrucksAccessInfo(Scanner sc, Vehicle vehicle){
        DeliveryTruckDao deliveryTruckDao = createDeliveryTruckDao();
        VehicleDao vehicleDao = createVehicleDao();

        System.out.println("Type of access: DELIVERY TRUCK");
        System.out.println("[1] Log in");
        System.out.println("[2] Register");
        int resMp = sc.nextInt();
        System.out.print("Enter your license plate: ");
        String licensePlate = captureAValidLicensePlate();

        if (resMp == 1) {
            vehicle = logInADeliveryTruck(deliveryTruckDao, licensePlate, vehicleDao);
        } else {
            vehicle = registerADeliveryTruck(deliveryTruckDao, licensePlate, vehicle);
        }
        return vehicle;
    }

    public static Vehicle logInADeliveryTruck(DeliveryTruckDao deliveryTruckDao, String licensePlate, VehicleDao vehicleDao) {
        while (deliveryTruckDao.findDeliveryTruckByLicensePlate(licensePlate) == null) {
            System.out.print("No monthly member with this license plate was found. Try again: ");
            licensePlate = captureAValidLicensePlate();
        }
        DeliveryTruck deliveryTruck = deliveryTruckDao.findDeliveryTruckByLicensePlate(licensePlate);
        return vehicleDao.findById(deliveryTruck.getVehicle().getId());
    }

    public static Vehicle registerADeliveryTruck(DeliveryTruckDao deliveryTruckDao, String licensePlate, Vehicle vehicle) {
        VehicleDao vehicleDao = createVehicleDao();

        if (vehicleDao.findByLicensePlate(licensePlate) != null) {
            if (vehicleDao.findByLicensePlate(licensePlate).getAccessType() == AccessType.MONTHLY_PAYER) {
                System.out.println("This license plate has already been registered in another category.");
                System.out.println("Program execution will terminate. Try again.");
                System.exit(1);
            } else {
                System.out.print("The license plate already has a registration. Enter your license plate again to log in: ");
                licensePlate = captureAValidLicensePlate();
                vehicle = logInADeliveryTruck(deliveryTruckDao, licensePlate, vehicleDao);
            }
        } else {
            vehicleDao.insert(vehicle);
            DeliveryTruck deliveryTruck = new DeliveryTruck(null, licensePlate, vehicle);
            deliveryTruckDao.insert(deliveryTruck);
        }
        return vehicle;
    }

    public static Ticket createTicketAccess(Vehicle vehicle) {
        TicketDao ticketDao = createTicketDao();
        VehicleDao vehicleDao = createVehicleDao();
        vehicleDao.insert(vehicle);
        Ticket ticket = new Ticket(null, vehicle);
        ticketDao.insert(ticket);
        return ticket;
    }

    public static void updateTicketInformation(Vehicle vehicle, Ticket ticket, int[] parkingSpaces){
        TicketDao ticketDao = createTicketDao();

        ticketDao.updateEntranceInformation(vehicle, ticket, parkingSpaces);
    }

    public static Vehicle capturePublicServiceAccessInfo(Vehicle vehicle) {
        VehicleDao vehicleDao = createVehicleDao();
        vehicleDao.insert(vehicle);

        return vehicle;
    }

    public static void finalizeMonthlyPayerAccess() {
        VehicleDao vehicleDao = createVehicleDao();
        MonthlyPayerDao monthlyPayerDao = createMonthlyPayerDao();

        System.out.print("Enter your license plate: ");
        String licensePlate;
        while (true) {
            licensePlate = captureAValidLicensePlate();
            if (monthlyPayerDao.findMonthlyPayerByLicensePlate(licensePlate) != null) {
                break;
            }
            System.out.print("No monthly member with this license plate was found. Try again: ");
        }
        MonthlyPayer monthlyPayer = monthlyPayerDao.findMonthlyPayerByLicensePlate(licensePlate);
        Vehicle vehicle = vehicleDao.findById(monthlyPayer.getVehicle().getId());
        vehicleDao.finalizeAccess(vehicle);
        emptyParkingSpace(vehicle);
    }

    public static void finalizeDeliveryTrucksAccess() {
        VehicleDao vehicleDao = createVehicleDao();
        DeliveryTruckDao deliveryTruckDao = createDeliveryTruckDao();

        System.out.print("Enter your license plate: ");
        String licensePlate;
        while (true) {
            licensePlate = captureAValidLicensePlate();
            if (deliveryTruckDao.findDeliveryTruckByLicensePlate(licensePlate) != null) {
                break;
            }
            System.out.print("No monthly member with this license plate was found. Try again: ");
        }
        DeliveryTruck deliveryTruck = deliveryTruckDao.findDeliveryTruckByLicensePlate(licensePlate);
        Vehicle vehicle = vehicleDao.findById(deliveryTruck.getVehicle().getId());
        vehicleDao.finalizeAccess(vehicle);
        emptyParkingSpace(vehicle);
    }

    public static void finalizeTicketAccess(Scanner sc, Integer exitGate) {
        TicketDao ticketDao = createTicketDao();
        VehicleDao vehicleDao = createVehicleDao();
        Vehicle vehicle;
        Ticket ticket;
        Integer ticketId;

        System.out.println("Inform your ticket number:");
        while(true){
            ticketId = sc.nextInt();
            if (ticketDao.findById(ticketId) != null) {
                break;
            }
            System.out.println("No ticket with this number was found. Try again:");
        }

        ticket = ticketDao.findById(ticketId);
        vehicle = vehicleDao.findById(ticket.getVehicle().getId());
        vehicle.setExitGate(exitGate);
        ticket.setVehicle(vehicle);
        ticketDao.updateExitInformation(ticket);
        ticket.calculateTotalValue();
        System.out.println(ticket);
        System.out.println("The total value was $" + ticket.getTotalValue() + ". Enter 1 to pay it.");
        Integer payment;

        while (true) {
            payment = sc.nextInt();
            if (payment == 1) {
                break;
            }
            System.out.println("Invalid input. Try again:");
        }
        emptyParkingSpace(ticket.getVehicle());
        ticketDao.deleteById(ticket.getId());
        vehicleDao.deleteById(ticket.getVehicle().getId());
    }
}

