/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.DiningTable;
import com.naigoapps.restaurant.model.Order;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author naigo
 */
public class OrderDao extends Dao {

    public List<Order> findByOrdination(String uuid) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("FROM Order rd where rd.ordination.uuid = :ordination", Order.class)
                .setParameter("ordination", uuid);
        List<Order> orders = q.getResultList();
        return orders;
    }

    public Order findByUuid(String uuid) {
        EntityManager em = getEntityManager();
        TypedQuery<Order> q = em.createQuery("FROM Order rd where rd.uuid = :uuid", Order.class)
                .setParameter("uuid", uuid);
        Order order = q.getSingleResult();
        return order;
    }

    public long countByDish(String dUuid) {
        Long result = getEntityManager().createQuery("SELECT count(*) FROM Order o where o.dish.uuid = :dParam", Long.class)
                .setParameter("dParam", dUuid).getSingleResult();
        return result;
    }
}
