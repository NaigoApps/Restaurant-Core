package com.naigoapps.restaurant.services.fiscal.hydra.fsms;

public class ItemSale {

    private String name;
    private int quantity;
    private double price;

    public ItemSale(String name, int quantity, double price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }
}
