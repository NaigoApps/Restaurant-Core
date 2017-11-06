/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.Phase;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author naigo
 */
public class PhaseDao extends Dao{

    public Phase findByUuid(String uuid){
        Query q = getEntityManager().createQuery("FROM Phase p where p.uuid = :uuid", Phase.class);
        q.setParameter("uuid", uuid);
        Phase phase = (Phase) q.getSingleResult();
        return phase;
    }
    
    public List<Phase> findAll(){
        EntityManager em = getEntityManager();
        Query q = em.createQuery("FROM Phase p ORDER BY p.name", Phase.class);
        List<Phase> phases = q.getResultList();
        return phases;
    }
}
