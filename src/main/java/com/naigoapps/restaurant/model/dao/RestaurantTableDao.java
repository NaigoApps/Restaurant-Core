/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.RestaurantTable;

/**
 *
 * @author naigo
 */
public class RestaurantTableDao extends AbstractDao<RestaurantTable> {

    @Override
    protected String getOrderByClause() {
        return "name";
    }

    @Override
    public Class<RestaurantTable> getEntityClass() {
        return RestaurantTable.class;
    }

}
