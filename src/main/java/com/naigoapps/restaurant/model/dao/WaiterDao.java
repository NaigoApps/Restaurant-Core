/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import java.util.List;

import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.naigoapps.restaurant.model.Waiter;
import com.naigoapps.restaurant.model.WaiterStatus;

/**
 *
 * @author naigo
 */
@Repository
public class WaiterDao extends AbstractDao<Waiter> {

    @Override
    protected String getOrderByClause() {
        return "name";
    }

    public List<Waiter> findActive() {
    	TypedQuery<Waiter> q = getEntityManager().createQuery("FROM Waiter w WHERE w.status = :status ORDER BY w.name", Waiter.class);
        q.setParameter("status", WaiterStatus.ACTIVE);
        List<Waiter> waiters = q.getResultList();
        return waiters;
    }

    @Override
    public Class<Waiter> getEntityClass() {
        return Waiter.class;
    }

}
