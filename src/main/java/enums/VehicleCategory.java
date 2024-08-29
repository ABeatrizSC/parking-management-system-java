package enums;

import java.util.HashSet;
import java.util.Set;

public enum VehicleCategory {
    CAR(2, createSet(1, 2, 3, 4, 5), createSet(6, 7, 8, 9, 10)),
    MOTOCYCLE(1, createSet(5), createSet(10)),
    DELIVERY_TRUCKS(4, createSet(1), createSet(6, 7, 8, 9, 10)),
    PUBLIC_SERVICE(0, createSet(1, 2, 3, 4, 5), createSet(6, 7, 8, 9, 10));

    private final int slotSize;
    private final Set<Integer> entranceGates = new HashSet<>();
    private final Set<Integer> exitGates = new HashSet<>();

    VehicleCategory(int slotSize, Set<Integer> entranceGates, Set<Integer> exitGates) {
        this.slotSize = slotSize;
        this.entranceGates.addAll(entranceGates);
        this.exitGates.addAll(exitGates);
    }

    public int getSlotSize() {
        return slotSize;
    }

    public Set<Integer> getEntranceGates() {
        return entranceGates;
    }

    public Set<Integer> getExitGates() {
        return exitGates;
    }

    private static Set<Integer> createSet(Integer... values) {
        return new HashSet<>(Set.of(values));
    }
}
