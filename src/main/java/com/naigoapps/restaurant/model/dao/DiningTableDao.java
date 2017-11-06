/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.DiningTable;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author naigo
 */
public class DiningTableDao extends Dao{
    
    public void removeByUuid(String uuid){
        Query q = getEntityManager().createQuery("DELETE FROM DiningTable t where t.uuid = :uuid");
        q.setParameter("uuid", uuid);
        q.executeUpdate();
    }
    
    public DiningTable findByUuid(String uuid){
        EntityManager em = getEntityManager();
        TypedQuery<DiningTable> q = em.createQuery("FROM DiningTable dt where dt.uuid = :uuidParam", DiningTable.class)
                .setParameter("uuidParam", uuid);
        DiningTable table = q.getSingleResult();
        return table;
    }
    
    public List<DiningTable> findByDate(LocalDate date){
        EntityManager em = getEntityManager();
        Query q = em.createQuery("FROM DiningTable dt where DATE(dt.date) = :dateParam", DiningTable.class)
                .setParameter("dateParam", date);
        List<DiningTable> tables = q.getResultList();
        return tables;
    }
    
    public List<DiningTable> findByWaiter(String wUuid){
        EntityManager em = getEntityManager();
        Query q = em.createQuery("FROM DiningTable dt where dt.waiter.uuid = :wParam", DiningTable.class)
                .setParameter("wParam", wUuid);
        List<DiningTable> tables = q.getResultList();
        return tables;
    }
}
