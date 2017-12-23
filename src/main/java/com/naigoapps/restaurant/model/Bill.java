/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author naigo
 */
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Bill extends BaseEntity {
    
    @ManyToOne
    private DiningTable table;
    
    @OneToMany(mappedBy = "bill")
    private List<Order> orders;

    public Bill() {
        orders = new ArrayList<>();
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
        orders.forEach(order -> {
            order.setBill(this);
        });
    }
    
    public void addOrder(Order order){
        if(!this.orders.contains(order)){
            this.orders.add(order);
            order.setBill(this);
        }
    }

    public void setTable(DiningTable table) {
        this.table = table;
        table.addBill(this);
    }

    public DiningTable getTable() {
        return table;
    }
    
    

}
