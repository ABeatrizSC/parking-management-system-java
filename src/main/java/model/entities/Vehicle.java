package model.entities;

import enums.AccessType;
import enums.VehicleCategory;


public class Vehicle {
    private Integer id;
    private VehicleCategory category;
    private Integer slotSize;
    private AccessType accessType;

    public Vehicle() {
    }

    public Vehicle(Integer id, VehicleCategory category, AccessType accessType) {
        this.id = id;
        this.category = category;
        slotSize = category.getSlotSize();
        this.accessType = accessType;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public VehicleCategory getCategory() {
        return category;
    }

    public void setCategory(VehicleCategory category) {
        this.category = category;
    }

    public Integer getSlotSize() {
        return slotSize;
    }

    public void setSlotSize(Integer slotSize) {
        this.slotSize = slotSize;
    }

    public AccessType getAccessType() {
        return accessType;
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", category=" + category +
                ", slotSize=" + slotSize +
                ", accessType=" + accessType;
    }

}
