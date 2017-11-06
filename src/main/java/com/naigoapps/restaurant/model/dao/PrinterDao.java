/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Dish;
import com.naigoapps.restaurant.model.Phase;
import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.RestaurantTable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author naigo
 */
public class PrinterDao extends Dao{

    public Printer findByUuid(String uuid){
        Query q = getEntityManager().createQuery("FROM Printer p where p.uuid = :uuid", Printer.class);
        q.setParameter("uuid", uuid);
        Printer printer = (Printer) q.getSingleResult();
        return printer;
    }
    
    public List<Printer> findAll(){
        EntityManager em = getEntityManager();
        Query q = em.createQuery("FROM Printer p ORDER BY p.name", Printer.class);
        List<Printer> printers = q.getResultList();
        return printers;
    }
}
