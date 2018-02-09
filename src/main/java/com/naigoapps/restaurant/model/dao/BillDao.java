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

    public int nextProgressive(LocalDate day) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery("SELECT max(b.progressive) FROM Bill b WHERE b.table.evening.day = :day");
        q.setParameter("day", day);
        Integer progressive = (Integer) q.getSingleResult();
        if (progressive != null) {
            return progressive + 1;
        }
        return 1;
    }

}
