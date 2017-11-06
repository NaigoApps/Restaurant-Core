/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Addition;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author naigo
 */
public class AdditionDao extends Dao{

    public void removeByUuid(String uuid){
        Query q = getEntityManager().createQuery("DELETE FROM Addition a where a.uuid = :uuid");
        q.setParameter("uuid", uuid);
        q.executeUpdate();
    }
    
    public Addition findByUuid(String uuid){
        Query q = getEntityManager().createQuery("FROM Addition a where a.uuid = :uuid", Addition.class);
        q.setParameter("uuid", uuid);
        Addition addition = (Addition) q.getSingleResult();
        return addition;
    }
    
    
    public List<Addition> findAll(){
        Query q = getEntityManager().createQuery("FROM Addition a ORDER BY a.name", Addition.class);
        List<Addition> additions = q.getResultList();
        return additions;
    }
}
