/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Location;
import com.naigoapps.restaurant.model.Printer;
import java.util.List;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

/**
 *
 * @author naigo
 */
@Repository
public class LocationDao extends AbstractDao<Location> {

    public List<Location> findByPrinter(Printer p) {
        TypedQuery<Location> q = getEntityManager().createQuery("FROM Location l where l.printer = :printer", Location.class);
        q.setParameter("printer", p);
        List<Location> locations = q.getResultList();
        return locations;
    }

    @Override
    public Class<Location> getEntityClass() {
        return Location.class;
    }

}
