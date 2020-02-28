/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.services.dto;

import java.util.UUID;

/**
 *
 * @author naigo
 */
public class DTO {

    private String uuid;

    public DTO() {
        this.uuid = UUID.randomUUID().toString();
    }

    public DTO(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

}
