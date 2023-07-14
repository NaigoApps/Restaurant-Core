/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Bill;
import com.naigoapps.restaurant.model.Evening;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 *
 * @author naigo
 */
@Repository
public class BillDao extends AbstractDao<Bill> {

    private static final String PROGRESSIVE_SELECT = "SELECT max(b.progressive) FROM Bill b WHERE ";

    public int nextBillProgressive(Evening evening) {
        EntityManager em = getEntityManager();
        Query q = em.createQuery(PROGRESSIVE_SELECT
                + "b.printTime IS NULL AND "
                + "b.table.evening = :evening");
        q.setParameter("evening", evening);
        Integer progressive = (Integer) q.getSingleResult();
        if (progressive != null) {
            return progressive + 1;
        }
        return 1;
    }

    @Override
    public Class<Bill> getEntityClass() {
        return Bill.class;
    }

}
