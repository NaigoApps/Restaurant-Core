/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Printer;
import com.naigoapps.restaurant.model.Settings;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author naigo
 */
public class PrinterDao extends Dao{
    
    @Inject
    private SettingsDao sDao;

    public List<Printer> findAll(){
        EntityManager em = getEntityManager();
        Query q = em.createQuery("FROM Printer p ORDER BY p.name", Printer.class);
        List<Printer> printers = q.getResultList();
        return printers;
    }
    
    public Printer findMainPrinter(){
        Settings s = sDao.find();
        if(s != null){
            return s.getMainPrinter();
        }
        return null;
    }
    
    public Printer findFiscalPrinter(){
        Settings s = sDao.find();
        if(s != null){
            return s.getFiscalPrinter();
        }
        return null;
    }
}
