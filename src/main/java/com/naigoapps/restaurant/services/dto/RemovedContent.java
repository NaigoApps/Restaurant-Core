package com.naigoapps.restaurant.services.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author naigo
 */
public class RemovedContent {

    private LocalDate date;
    private List<BillDTO> bills;
    private List<OrderDTO> orders;

    public RemovedContent() {
        this.bills = new ArrayList<>();
        this.orders = new ArrayList<>();
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void addBills(List<BillDTO> bills) {
        this.bills.addAll(bills);
    }

    public void addOrders(List<OrderDTO> orders) {
        this.orders.addAll(orders);
    }

    public void setBills(List<BillDTO> bills) {
        this.bills = bills;
    }

    public void setOrders(List<OrderDTO> orders) {
        this.orders = orders;
    }

    public List<BillDTO> getBills() {
        return bills;
    }

    public List<OrderDTO> getOrders() {
        return orders;
    }

}
