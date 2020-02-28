/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import org.springframework.stereotype.Repository;

import com.naigoapps.restaurant.model.Customer;

/**
 *
 * @author naigo
 */
@Repository
public class CustomerDao extends AbstractDao<Customer> {

    @Override
    protected String getOrderByClause() {
        return "name, surname";
    }

    @Override
    public Class<Customer> getEntityClass() {
        return Customer.class;
    }

}
