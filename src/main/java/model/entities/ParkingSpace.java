package model.entities;

import enums.SlotType;

import java.util.Objects;

public class ParkingSpace {
    private Integer id;
    private SlotType type;
    private static Boolean isOcuppied;

    public ParkingSpace(Integer id, SlotType type) {
        this.id = id;
        this.type = type;
        isOcuppied = false;
    }

    public Integer getId() {
        return id;
    }

    public Boolean getOcuppied() {
        return isOcuppied;
    }

    public static void occupyParkingSpace(){
        isOcuppied = true;
    }

    public static void emptyParkingSpace(){
        isOcuppied = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ParkingSpace that = (ParkingSpace) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
