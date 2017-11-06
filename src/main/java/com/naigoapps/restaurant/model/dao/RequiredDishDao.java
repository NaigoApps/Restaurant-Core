/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Ordination;
import com.naigoapps.restaurant.model.RequiredDish;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author naigo
 */
public class RequiredDishDao extends Dao{
    
    public List<RequiredDish> findByOrdination(String uuid){
        EntityManager em = getEntityManager();
        Query q = em.createQuery("FROM RequiredDish rd where rd.ordination.uuid = :ordination", RequiredDish.class)
                .setParameter("ordination", uuid);
        List<RequiredDish> orders = q.getResultList();
        return orders;
    }
    
    public RequiredDish findByUuid(String uuid){
        EntityManager em = getEntityManager();
        TypedQuery<RequiredDish> q = em.createQuery("FROM RequiredDish rd where rd.uuid = :uuid", RequiredDish.class)
                .setParameter("uuid", uuid);
        RequiredDish order = q.getSingleResult();
        return order;
    }
}
