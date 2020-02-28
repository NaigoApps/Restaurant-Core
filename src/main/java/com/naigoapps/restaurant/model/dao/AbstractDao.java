/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.transaction.annotation.Transactional;

import com.naigoapps.restaurant.model.BaseEntity;

/**
 *
 * @author naigo
 * @param <E>
 */

public abstract class AbstractDao<E extends BaseEntity> implements Dao<E>{

    @PersistenceContext(name = "restaurant-pu")
    private EntityManager em;

    @Transactional
    public void persist(BaseEntity... entities) {
        Arrays.stream(entities).forEach(entity -> em.persist(entity));
    }

    public EntityManager getEntityManager() {
        return em;
    }

    public void setEntityManager(EntityManager em) {
        this.em = em;
    }
    
    

    @Override
    public E findByUuid(String uuid) {
        try {
            TypedQuery<E> q = getEntityManager().createQuery("FROM " + getEntityClass().getName() + " e where e.uuid = :uuid", getEntityClass());
            q.setParameter("uuid", uuid);
            E entity = q.getSingleResult();
            return entity;
        } catch (Exception ex) {
            Logger.getGlobal().log(Level.SEVERE, "Error during entity retrieve", ex);
        }
        return null;
    }

    public List<E> findWhere(String condition) {
        String query = "FROM " + getEntityClass().getName();
        if(condition != null) {
        	query += " WHERE " + condition;
        }
        if (getOrderByClause() != null) {
            query += " ORDER BY " + getOrderByClause();
        }
        TypedQuery<E> q = getEntityManager().createQuery(query, getEntityClass());
        return q.getResultList();
    }

    @Override
    public List<E> findAll() {
        String query = "FROM " + getEntityClass().getName();
        if (getOrderByClause() != null) {
            query += " ORDER BY " + getOrderByClause();
        }
        TypedQuery<E> q = getEntityManager().createQuery(query, getEntityClass());
        return q.getResultList();
    }

    @Override
    public void deleteByUuid(String uuid) {
        Query q = getEntityManager().createQuery("DELETE FROM " + getEntityClass().getName() + " e where e.uuid = :uuid");
        q.setParameter("uuid", uuid);
        q.executeUpdate();
    }

    @Override
    public void delete(BaseEntity entity) {
        em.remove(entity);
    }

    protected String getOrderByClause() {
        return null;
    }

}
