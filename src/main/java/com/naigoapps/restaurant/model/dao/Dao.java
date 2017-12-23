/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.BaseEntity;
import com.naigoapps.restaurant.model.Printer;
import java.util.Arrays;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
    public void persist(BaseEntity... entities) {
        Arrays.stream(entities).forEach(entity -> em.persist(entity));
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public <E> E findByUuid(String uuid, Class<E> type) {
        try {
            TypedQuery<E> q = getEntityManager().createQuery("FROM " + type.getName() + " e where e.uuid = :uuid", type);
            q.setParameter("uuid", uuid);
            E entity = q.getSingleResult();
            return entity;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public void deleteByUuid(String uuid, Class c){
        Query q = getEntityManager().createQuery("DELETE FROM " + c.getName() + " e where e.uuid = :uuid");
        q.setParameter("uuid", uuid);
        q.executeUpdate();
    }

    public void delete(BaseEntity entity){
        em.remove(entity);
    }
    
}
