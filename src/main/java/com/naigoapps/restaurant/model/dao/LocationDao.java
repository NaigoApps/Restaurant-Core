/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Location;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author naigo
 */
public class LocationDao extends Dao{
    
    public List<Location> findAll(){
        Query q = getEntityManager().createQuery("FROM Location l ORDER BY l.name", Location.class);
        List<Location> locations = q.getResultList();
        return locations;
    }
    
    public Location findByUuid(String uuid){
        Query q = getEntityManager().createQuery("FROM Location l where l.uuid = :uuid", Location.class);
        q.setParameter("uuid", uuid);
        Location location = (Location) q.getSingleResult();
        return location;
    }
    
    public void removeByUuid(String uuid){
        Query q = getEntityManager().createQuery("DELETE FROM Location l where l.uuid = :uuid");
        q.setParameter("uuid", uuid);
        q.executeUpdate();
    }
}
