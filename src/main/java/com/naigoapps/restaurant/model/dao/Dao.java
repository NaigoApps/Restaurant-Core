/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.BaseEntity;
import java.util.Arrays;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

/**
 *
 * @author naigo
 */
@Generic
public class Dao {

    @PersistenceContext(name = "restaurant-pu")
    private EntityManager em;
    
    @Transactional
    public void persist(BaseEntity... entities){
        Arrays.stream(entities).forEach(entity -> em.persist(entity));
    }
    
    public EntityManager getEntityManager() {
        return em;
    }

}
