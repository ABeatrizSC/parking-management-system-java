package model.entities;

import enums.VehicleCategory;

import java.util.*;

import static model.entities.ParkingSpace.emptyParkingSpace;
import static model.entities.ParkingSpace.occupyParkingSpace;

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

    public static void operateGate(GateType type, Vehicle vehicle, Integer selectionedGate){
        System.out.println("entradas de acordo: " + vehicle.getCategory().getEntranceGates());
        System.out.println("saidas de acordo: " + vehicle.getCategory().getExitGates());
        if (type == GateType.ENTRANCE) {
            if (vehicle.getCategory().getEntranceGates().contains(selectionedGate)) {
                vehicle.setEntranceGate(selectionedGate);
                occupyParkingSpace();
            } else {
                System.out.println("Esta cancela não pode ser acessada pelo seu tipo de veículo. Tente outra.");
            }
        } else {
            if (vehicle.getCategory().getExitGates().contains(selectionedGate)) {
                vehicle.setExitGate(selectionedGate);
                emptyParkingSpace();
            } else {
                System.out.println("Esta cancela não pode ser acessada pelo seu tipo de veículo. Tente outra.");
            }
        }
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
