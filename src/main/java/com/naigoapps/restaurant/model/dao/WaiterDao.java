/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Waiter;
import com.naigoapps.restaurant.model.WaiterStatus;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author naigo
 */
public class WaiterDao extends Dao{
    
    public void removeByUuid(String uuid){
        Query q = getEntityManager().createQuery("DELETE FROM Waiter w where w.uuid = :uuid");
        q.setParameter("uuid", uuid);
        q.executeUpdate();
    }
    
    public Waiter findByUuid(String uuid){
        Query q = getEntityManager().createQuery("FROM Waiter w where w.uuid = :uuid", Waiter.class);
        q.setParameter("uuid", uuid);
        Waiter waiter = (Waiter) q.getSingleResult();
        return waiter;
    }
    
    
    public List<Waiter> findAll(){
        Query q = getEntityManager().createQuery("FROM Waiter w ORDER BY w.name", Waiter.class);
        List<Waiter> waiters = q.getResultList();
        return waiters;
    }
    
    
    public List<Waiter> findActive(){
        Query q = getEntityManager().createQuery("FROM Waiter w WHERE w.status = :status ORDER BY w.name", Waiter.class);
        q.setParameter("status", WaiterStatus.ACTIVE);
        List<Waiter> waiters = q.getResultList();
        return waiters;
    }
}
