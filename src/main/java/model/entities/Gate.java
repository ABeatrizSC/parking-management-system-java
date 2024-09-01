package model.entities;

import enums.VehicleCategory;
import model.dao.VehicleDao;

import java.util.*;

import static model.entities.Parking.chooseAEntranceGate;

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

    public static void operateGate(GateType type, Vehicle vehicle, VehicleDao vehicleDao, Scanner sc){
        Integer selectionedGate;
        if (type == GateType.ENTRANCE) {
            while (true) {
                selectionedGate = chooseAEntranceGate(sc);
                if (vehicle.getCategory().getEntranceGates().contains(selectionedGate.toString())) {
                    vehicle.setEntranceGate(selectionedGate);
                    //occupyParkingSpace();
                    vehicleDao.updateSelectionedGate(vehicle, selectionedGate);
                    break;
                }
                System.out.println("This gate can't be accessed due to your vehicle type. Try another one.");
            }
        } else {
            while(true) {
                selectionedGate = chooseAEntranceGate(sc);
                if (vehicle.getCategory().getExitGates().contains(selectionedGate.toString())) {
                    vehicle.setExitGate(selectionedGate);
                    //emptyParkingSpace();
                    break;
                }
                System.out.println("This gate can't be accessed due to your vehicle type. Try another one.");
            }
        }
        vehicle.setEntranceGate(selectionedGate);
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
