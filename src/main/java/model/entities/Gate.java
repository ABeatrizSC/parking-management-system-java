package model.entities;

import enums.VehicleCategory;
import model.dao.VehicleDao;

import java.util.*;

import static model.entities.Parking.chooseAEntranceGate;
import static model.entities.Parking.chooseAExitGate;

public class Gate {
    private Integer id;
    private GateType type;

    Vehicle vehicle;

    public enum GateType {
        ENTRANCE(Arrays.asList(1, 2, 3, 4, 5)),
        EXIT(Arrays.asList(6, 7, 8, 9, 10));

        private final List<Integer> gateNumbers;

        GateType(List<Integer> gateNumbers) {
            this.gateNumbers = gateNumbers;
        }

        public List<Integer> getGateNumbers() {
            return gateNumbers;
        }
    }

    protected Gate(Integer id, GateType type) {
        this.id = id;
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public GateType getType() {
        return type;
    }

    public static void operateEntranceGate(Vehicle vehicle, VehicleDao vehicleDao, Scanner sc){
        Integer selectionedGate;
        while (true) {
            selectionedGate = chooseAEntranceGate(sc);
            if (vehicle.getCategory().getEntranceGates().contains(selectionedGate.toString())) {
                vehicle.setEntranceGate(selectionedGate);
                vehicleDao.updateSelectionedGate(vehicle, selectionedGate);
                break;
            }
            System.out.println("This gate can't be accessed due to your vehicle type. Try another one.");
        }
    }

    public static Integer operateExitGate(Scanner sc, VehicleCategory vehicleCategory) {
        Integer selectionedGate;
        List<Integer> exitGates = GateType.EXIT.getGateNumbers();
        while(true) {
            selectionedGate = chooseAExitGate(sc);
            if (vehicleCategory.getExitGates().contains(exitGates.get(selectionedGate-1).toString())) {
                break;
            }
            System.out.println("This gate can't be accessed due to your vehicle type. Try another one.");
        }
        return exitGates.get(selectionedGate-1);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Gate gate = (Gate) o;
        return type == gate.type;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type);
    }
}
