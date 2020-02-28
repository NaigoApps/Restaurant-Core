/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author naigo
 */
@Entity
@Table(name = "dining_tables")
public class DiningTable extends BaseEntity {

    private int coverCharges;

    @ManyToOne
    private Evening evening;

    @ManyToOne
    private Waiter waiter;

    @OneToMany(mappedBy = "table")
    private List<Ordination> ordinations;

    @OneToMany(mappedBy = "table")
    private List<Bill> bills;

    private LocalDateTime openingTime;

    @ManyToOne
    private RestaurantTable table;

    private DiningTableStatus status;

    public DiningTable() {
        ordinations = new ArrayList<>();
        bills = new ArrayList<>();
    }

    public Waiter getWaiter() {
        return waiter;
    }

    public void setWaiter(Waiter waiter) {
        this.waiter = waiter;
    }

    public List<Ordination> getOrdinations() {
        return ordinations;
    }

    public List<Order> getOrders() {
        return collectOrders();
    }

    private List<Order> collectOrders() {
        List<Order> result = new ArrayList<>();
        ordinations.forEach(ordination -> result.addAll(ordination.getOrders()));
        return result;
    }

    public void setOrdinations(List<Ordination> orders) {
        this.ordinations = orders;
        orders.forEach(order -> {
            order.setTable(this);
        });
    }

    public void addOrdination(Ordination ordination) {
        if (!this.ordinations.contains(ordination)) {
            this.ordinations.add(ordination);
            ordination.setTable(this);
        }
    }

    public void removeOrdination(Ordination ordination) {
        if (this.ordinations.contains(ordination)) {
            this.ordinations.remove(ordination);
            ordination.setTable(null);
        }
    }

    public void setCoverCharges(int coverCharges) {
        this.coverCharges = coverCharges;
    }

    public int getCoverCharges() {
        return coverCharges;
    }

    public void setOpeningTime(LocalDateTime date) {
        this.openingTime = date;
    }

    public LocalDateTime getOpeningTime() {
        return openingTime;
    }

    public RestaurantTable getTable() {
        return table;
    }

    public void setTable(RestaurantTable table) {
        this.table = table;
    }

    public Evening getEvening() {
        return evening;
    }

    public void setEvening(Evening evening) {
        if (this.evening != null && evening == null) {
            this.evening.removeDiningTable(this);
        }
        this.evening = evening;
        if (evening != null) {
            evening.addDiningTable(this);
        }
    }

    public float getTotalPrice() {
        return coverCharges * evening.getCoverCharge()
                + collectOrders().stream()
                .map(Order::getPrice)
                .reduce(0.0f, (p1, p2) -> p1 + p2);
    }

    public List<Bill> getBills() {
        return bills;
    }

    public void setBills(List<Bill> bills) {
        this.bills = bills;
        bills.forEach(bill -> {
            bill.setTable(this);
        });
    }

    public void addBill(Bill bill) {
        if (!this.bills.contains(bill)) {
            this.bills.add(bill);
            bill.setTable(this);
        }
    }

    public DiningTableStatus getStatus() {
        return status;
    }

    public void setStatus(DiningTableStatus status) {
        this.status = status;
    }

    public void updateStatus() {
        if (this.bills.isEmpty()) {
            this.status = DiningTableStatus.OPEN;
        } else {
            this.status = DiningTableStatus.CLOSING;
            if (getOrders().stream()
                    .noneMatch(order -> order.getBill() == null ||
                            order.getBill().getPrintDate() == null)) {
                this.status = DiningTableStatus.CLOSED;
            }
        }
    }

    public boolean canBeClosed() {
        return remainingCoverCharges() == 0 &&
                collectOrders().stream().allMatch(o -> o.getBill() != null);
    }

    public int remainingCoverCharges() {
        return coverCharges - bills.stream()
                .mapToInt(Bill::getCoverCharges)
                .sum();
    }
}
