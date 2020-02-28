package com.naigoapps.restaurant.services.rs;

import java.util.List;

public class CreateBillParams {
    private int ccs;
    private List<String> orders;
    private float total;

    public void setTotal(float total) {
        this.total = total;
    }

    public float getTotal() {
        return total;
    }

    public int getCcs() {
        return ccs;
    }

    public void setCcs(int ccs) {
        this.ccs = ccs;
    }

    public List<String> getOrders() {
        return orders;
    }

    public void setOrders(List<String> orders) {
        this.orders = orders;
    }
}
