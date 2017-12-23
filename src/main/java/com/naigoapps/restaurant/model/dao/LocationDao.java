/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Location;
import com.naigoapps.restaurant.model.Printer;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

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
    
    public List<Location> findByPrinter(Printer p){
        TypedQuery<Location> q = getEntityManager().createQuery("FROM Location l where l.printer = :printer", Location.class);
        q.setParameter("printer", p);
        List<Location> locations = q.getResultList();
        return locations;
    }
    
    public void removeByUuid(String uuid){
        Query q = getEntityManager().createQuery("DELETE FROM Location l where l.uuid = :uuid");
        q.setParameter("uuid", uuid);
        q.executeUpdate();
    }
}
