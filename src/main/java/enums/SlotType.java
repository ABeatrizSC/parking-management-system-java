package enums;

public enum SlotType {
    MONTHLY(200),
    CASUAL(300);

    private final int quantity;

    SlotType(int quantity){
        this.quantity = quantity;
    }

    public int getQuantity(){
        return quantity;
    }
}
