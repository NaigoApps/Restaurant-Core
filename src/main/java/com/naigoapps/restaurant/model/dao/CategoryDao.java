/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Category;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author naigo
 */
public class CategoryDao extends Dao{

    public void removeByUuid(String uuid){
        Query q = getEntityManager().createQuery("DELETE FROM Category c where c.uuid = :uuid");
        q.setParameter("uuid", uuid);
        q.executeUpdate();
    }
    
    public Category findByUuid(String uuid){
        Query q = getEntityManager().createQuery("FROM Category c where c.uuid = :uuid", Category.class);
        q.setParameter("uuid", uuid);
        Category category = (Category) q.getSingleResult();
        return category;
    }
    
    
    public List<Category> findAll(){
        Query q = getEntityManager().createQuery("FROM Category c ORDER BY c.name", Category.class);
        List<Category> categories = q.getResultList();
        return categories;
    }
}
