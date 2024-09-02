package enums;

import java.util.HashSet;
import java.util.Set;

public enum AccessType {
    MONTHLY_PAYER(createCategoriesSet(VehicleCategory.CAR, VehicleCategory.MOTOCYCLE)),
    TICKET(createCategoriesSet(VehicleCategory.CAR, VehicleCategory.MOTOCYCLE)),
    DELIVERY_TRUCKS(createCategoriesSet(VehicleCategory.DELIVERY_TRUCKS)),
    PUBLIC_SERVICE(createCategoriesSet(VehicleCategory.PUBLIC_SERVICE));

    private final Set<VehicleCategory> categoriesAvailable = new HashSet<VehicleCategory>();

    AccessType(Set<VehicleCategory> categoriesAvailable) {
        this.categoriesAvailable.addAll(categoriesAvailable);
    }

    public Set<VehicleCategory> getCategoriesAvailable() {
        return categoriesAvailable;
    }

    private static Set<VehicleCategory> createCategoriesSet(VehicleCategory... values) {
        return new HashSet<>(Set.of(values));
    }
}
