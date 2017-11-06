/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Dish;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author naigo
 */
public class DishDao extends Dao{

    public void removeByUuid(String uuid){
        getEntityManager().remove(findByUuid(uuid));
    }
    
    public Dish findByUuid(String uuid){
        Query q = getEntityManager().createQuery("FROM Dish d where d.uuid = :uuid", Dish.class);
        q.setParameter("uuid", uuid);
        Dish dish = (Dish) q.getSingleResult();
        return dish;
    }
    
    public List<Dish> findByCategory(String category){
        Query q = getEntityManager().createQuery("FROM Dish d where d.category.uuid = :category", Dish.class);
        q.setParameter("category", category);
        return q.getResultList();
    }
    
    public List<Dish> findAll(){
        EntityManager em = getEntityManager();
        Query q = em.createQuery("FROM Dish d ORDER BY d.name", Dish.class);
        List<Dish> tables = q.getResultList();
        return tables;
    }
}
