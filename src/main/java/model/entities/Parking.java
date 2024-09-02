package model.entities;

import enums.AccessType;
import enums.SlotType;
import enums.VehicleCategory;
import model.dao.*;

import java.util.*;

import static UI.Colors.*;
import static model.dao.DaoFactory.*;

public class Parking {
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
            if (Objects.equals(parkingSpaces, "1")){
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
                    System.out.println("These parking spaces are exclusively for casual visitors or don't exist. \nCheck if there is the number of sequential parking spaces required for your vehicle and try again:\n(If there are none, press 1 to exit)");
                    return false;
                }
            }
        } else {
            for (int space : spaces){
                if (space < 201 || space > SlotType.CASUAL.getQuantity()) {
                    System.out.println("These parking spaces are exclusive to monthly members or don't exist. \nCheck if there is the number of sequential parking spaces required for your vehicle and try again:\n(If there are none, press 1 to exit)");
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
            System.out.println("Error: Please enter valid numbers for parking spaces. \nCheck if there is the number of sequential parking spaces required for your vehicle and try again:\n(If there are none, press 1 to exit)");
            return false;
        }
        return true;
    }

    private static Boolean isCorrectNumberOfSpaces(int[] spaces, Vehicle vehicle) {
        if (spaces.length != vehicle.getSlotSize()) {
            System.out.println("Error: Incorrect number of parking spaces. Expected: " + vehicle.getSlotSize() + ". \nCheck if there is the number of sequential parking spaces required for your vehicle and try again:\n(If there are none, press 1 to exit)");
            return false;
        }
        return true;
    }

    private static Boolean areAnySpacesOccupied(int[] spaces, ParkingSpaceDao parkingSpaceDao) {
        for (int i = 0; i < spaces.length; i++) {
            ParkingSpace parkingSpace = parkingSpaceDao.findById(spaces[i]);
            if (parkingSpace != null && parkingSpace.getIsOccupied()) {
                System.out.println("Error: Parking Space " + spaces[i] + " is already occupied. \nCheck if there is the number of sequential parking spaces required for your vehicle and try again:\n(If there are none, press 1 to exit)");
                return true;
            }
        }
        return false;
    }

    private static Boolean areSpacesSequential(int[] spaces) {
        Arrays.sort(spaces);
        for (int i = 1; i < spaces.length; i++) {
            if (spaces[i] != spaces[i - 1] + 1) {
                System.out.println("Error: Parking spaces are not sequential. \nCheck if there is the number of sequential parking spaces required for your vehicle and try again:\n(If there are none, press 1 to exit)");
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

    public static void emptyParkingSpace(int[] parkingSpaces, Vehicle vehicle){
        ParkingSpaceDao parkingSpaceDao = createParkingSpaceDao();
        ParkingSpace parkingSpace;
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
        return vehicleDao.findByLicensePlate(licensePlate);
    }

    public static Vehicle registerAMonthlyPayer(MonthlyPayerDao monthlyPayerDao, String licensePlate, Vehicle vehicle) {
        VehicleDao vehicleDao = createVehicleDao();

        if (vehicleDao.findByLicensePlate(licensePlate) != null) {
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
        return vehicleDao.findByLicensePlate(licensePlate);
    }

    public static Vehicle registerADeliveryTruck(DeliveryTruckDao deliveryTruckDao, String licensePlate, Vehicle vehicle) {
        VehicleDao vehicleDao = createVehicleDao();

        if (vehicleDao.findByLicensePlate(licensePlate) != null) {
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

    public static Ticket createTicketAccess(Vehicle vehicle) {
        TicketDao ticketDao = createTicketDao();
        VehicleDao vehicleDao = createVehicleDao();
        vehicleDao.insert(vehicle);
        System.out.println(vehicle);
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
}

