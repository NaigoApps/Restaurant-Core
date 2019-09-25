/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import java.util.List;

import com.naigoapps.restaurant.model.Addition;

/**
 *
 * @author naigo
 */
public class AdditionDao extends AbstractDao<Addition>{

    @Override
    protected String getOrderByClause() {
        return "name";
    }

    @Override
    public Class<Addition> getEntityClass() {
        return Addition.class;
    }
    
    public List<Addition> findGeneric(){
    	return findWhere("generic = true");
    }
    
    
}
