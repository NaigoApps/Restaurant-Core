/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import java.time.LocalDate;
import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author naigo
 */
public class BillDao extends Dao {
    
    public int nextBillProgressive(LocalDate day) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT max(b.progressive) FROM Bill b WHERE "
                + "b.customer IS NULL AND "
                + "b.printTime IS NULL AND "
                + "b.table.evening.day = :day");
        q.setParameter("day", day);
        Integer progressive = (Integer) q.getSingleResult();
        if (progressive != null) {
            return progressive + 1;
        }
        return 1;
    }

    public int nextReceiptProgressive(LocalDate day) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT max(b.progressive) FROM Bill b WHERE "
                + "b.customer IS NULL AND "
                + "b.printTime IS NOT NULL AND "
                + "b.table.evening.day = :day");
        q.setParameter("day", day);
        Integer progressive = (Integer) q.getSingleResult();
        if (progressive != null) {
            return progressive + 1;
        }
        return 1;
    }
    
    public int nextInvoiceProgressive(LocalDate day) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT max(b.progressive) FROM Bill b WHERE "
                + "b.customer IS NOT NULL AND "
                + "b.printTime IS NOT NULL AND "
                + "YEAR(b.table.evening.day) = :year");
        q.setParameter("year", day.getYear());
        Integer progressive = (Integer) q.getSingleResult();
        if (progressive != null) {
            return progressive + 1;
        }
        return 1;
    }

}
