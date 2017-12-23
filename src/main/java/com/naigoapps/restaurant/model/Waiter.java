/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author naigo
 */
@Entity
@Table(name = "waiters")
public class Waiter extends BaseEntity {

    private String name;
    private String surname;
    private String cf;
    private WaiterStatus status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public WaiterStatus getStatus() {
        return status;
    }

    public void setStatus(WaiterStatus status) {
        this.status = status;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

}
