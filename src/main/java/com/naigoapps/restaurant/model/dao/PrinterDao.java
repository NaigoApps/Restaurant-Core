/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Printer;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author naigo
 */
public class PrinterDao extends Dao{

    public List<Printer> findAll(){
        EntityManager em = getEntityManager();
        Query q = em.createQuery("FROM Printer p ORDER BY p.name", Printer.class);
        List<Printer> printers = q.getResultList();
        return printers;
    }
    
    public Printer findMainPrinter(){
        EntityManager em = getEntityManager();
        Query q = em.createQuery("FROM Printer p WHERE p.main=true", Printer.class);
        List<Printer> printers = q.getResultList();
        return printers.size() > 0 ? printers.get(0) : null;
    }
}
