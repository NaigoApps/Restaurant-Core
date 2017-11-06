/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.RestaurantTable;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author naigo
 */
public class RestaurantTableDao extends Dao{
    
    public List<RestaurantTable> findAll(){
        Query q = getEntityManager().createQuery("FROM RestaurantTable rt ORDER BY rt.name", RestaurantTable.class);
        List<RestaurantTable> tables = q.getResultList();
        return tables;
    }
    
    public RestaurantTable findByUuid(String uuid){
        Query q = getEntityManager().createQuery("FROM RestaurantTable rt where rt.uuid = :uuid", RestaurantTable.class);
        q.setParameter("uuid", uuid);
        RestaurantTable table = (RestaurantTable) q.getSingleResult();
        return table;
    }
    
    public void removeByUuid(String uuid){
        Query q = getEntityManager().createQuery("DELETE FROM RestaurantTable rt where rt.uuid = :uuid");
        q.setParameter("uuid", uuid);
        q.executeUpdate();
    }
}
