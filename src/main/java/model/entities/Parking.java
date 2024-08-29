package model.entities;

import enums.AccessType;
import enums.VehicleCategory;

import java.util.Scanner;

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
}
