/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import org.springframework.stereotype.Repository;

import com.naigoapps.restaurant.model.UTEntry;

/**
 *
 * @author naigo
 */
@Repository
public class UTDao extends AbstractDao<UTEntry>{

    @Override
    public Class<UTEntry> getEntityClass() {
        return UTEntry.class;
    }

    @Override
    protected String getOrderByClause() {
        return "actionTime";
    }
    
    
    
}
