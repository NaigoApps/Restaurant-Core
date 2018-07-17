/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.Order;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author naigo
 */
public class OrderDao extends AbstractDao<Order> {

    public List<Order> findByOrdination(String uuid) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("FROM Order rd where rd.ordination.uuid = :ordination", Order.class)
                .setParameter("ordination", uuid);
        List<Order> orders = q.getResultList();
        return orders;
    }

    public long countByDish(Dish d) {
        Long result = getEntityManager().createQuery("SELECT count(*) FROM Order o where o.dish = :dParam", Long.class)
                .setParameter("dParam", d).getSingleResult();
        return result;
    }

    @Override
    public Class<Order> getEntityClass() {
        return Order.class;
    }
    
}
