/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Phase;

/**
 *
 * @author naigo
 */
public class PhaseDao extends AbstractDao<Phase> {

    @Override
    protected String getOrderByClause() {
        return "name";
    }

    @Override
    public Class<Phase> getEntityClass() {
        return Phase.class;
    }

}
