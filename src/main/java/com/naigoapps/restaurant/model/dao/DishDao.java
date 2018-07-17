/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Dish;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author naigo
 */
public class DishDao extends AbstractDao<Dish>{
    
    public List<Dish> findByCategory(String category){
        Query q = getEntityManager().createQuery("FROM Dish d where d.category.uuid = :category", Dish.class);
        q.setParameter("category", category);
        return q.getResultList();
    }

    @Override
    protected String getOrderByClause() {
        return "name";
    }

    @Override
    public Class<Dish> getEntityClass() {
        return Dish.class;
    }
    
    
}
