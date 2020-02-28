/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.naigoapps.restaurant.model.DiningTable;

/**
 *
 * @author naigo
 */
@Repository
public class DiningTableDao extends AbstractDao<DiningTable> {

    public List<DiningTable> findByDate(LocalDate date) {
        EntityManager em = getEntityManager();
        TypedQuery<DiningTable> q = em.createQuery("FROM DiningTable dt where DATE(dt.date) = :dateParam", DiningTable.class)
                .setParameter("dateParam", date);
        List<DiningTable> tables = q.getResultList();
        return tables;
    }

    public List<DiningTable> findByWaiter(String wUuid) {
        EntityManager em = getEntityManager();
        TypedQuery<DiningTable> q = em.createQuery("FROM DiningTable dt where dt.waiter.uuid = :wParam", DiningTable.class)
                .setParameter("wParam", wUuid);
        List<DiningTable> tables = q.getResultList();
        return tables;
    }

    @Override
    public Class<DiningTable> getEntityClass() {
        return DiningTable.class;
    }
    
    
    
}
