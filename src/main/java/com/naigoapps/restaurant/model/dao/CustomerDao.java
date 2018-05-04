/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Customer;
import com.naigoapps.restaurant.model.Location;
import com.naigoapps.restaurant.model.Printer;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author naigo
 */
public class CustomerDao extends Dao{
    
    public List<Customer> findAll(){
        Query q = getEntityManager().createQuery("FROM Customer c ORDER BY c.name, c.surname", Customer.class);
        List<Customer> customers = q.getResultList();
        return customers;
    }
    
}
