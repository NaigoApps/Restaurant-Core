/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

/**
 *
 * @author naigo
 */
public class CustomerDTO extends DTO{
    private String name;
    private String surname;
    private String piva;
    private String cf;
    private String address;
    private String city;
    private String cap;
    private String district;

    public CustomerDTO() {
    }

    public CustomerDTO(String uuid, String name, String surname, String piva, String cf, String address, String city, String cap, String district) {
        super(uuid);
        this.name = name;
        this.surname = surname;
        this.piva = piva;
        this.cf = cf;
        this.address = address;
        this.city = city;
        this.cap = cap;
        this.district = district;
    }

    
    
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

    public String getPiva() {
        return piva;
    }

    public void setPiva(String piva) {
        this.piva = piva;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    
}
