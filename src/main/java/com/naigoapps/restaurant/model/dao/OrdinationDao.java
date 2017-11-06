/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Ordination;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author naigo
 */
public class OrdinationDao extends Dao{

    
    public void removeByUuid(String uuid){
        Query q = getEntityManager().createQuery("DELETE FROM Ordination o where o.uuid = :uuid");
        q.setParameter("uuid", uuid);
        q.executeUpdate();
    }
    
    
    public Ordination findByUuid(String uuid){
        TypedQuery<Ordination> q = getEntityManager().createQuery("FROM Ordination o where o.uuid = :uuid", Ordination.class);
        q.setParameter("uuid", uuid);
        Ordination o = q.getSingleResult();
        return o;
    }
    
    public List<Ordination> findByDiningTable(String uuid){
        TypedQuery<Ordination> q = getEntityManager().createQuery("FROM Ordination o where o.table.uuid = :uuid", Ordination.class);
        q.setParameter("uuid", uuid);
        List<Ordination> o = q.getResultList();
        return o;
    }
    
    public int nextProgressive(LocalDate date){
        TypedQuery<Integer> q = getEntityManager().createQuery(
                "SELECT max(o.progressive) FROM Ordination o WHERE o.table.evening.day = :date", Integer.class);
        q.setParameter("date", date);
        Integer progressive = q.getSingleResult();
        if(progressive != null){
            return progressive + 1;
        }else{
            return 1;
        }
    }
}
