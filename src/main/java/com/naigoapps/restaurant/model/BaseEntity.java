/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model;

import java.util.Objects;
import java.util.UUID;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author naigo
 */
@MappedSuperclass
public class BaseEntity {
    @Id
    @GeneratedValue
    private int id;
    private String uuid;

    public BaseEntity() {
        this.uuid = UUID.randomUUID().toString();
    }

    public int getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    @Override
    public boolean equals(Object obj) {
        return uuid != null && obj instanceof BaseEntity && uuid.equals(((BaseEntity)obj).getUuid());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.uuid);
        return hash;
    }
    
    
}
