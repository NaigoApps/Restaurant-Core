/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Evening;
import com.naigoapps.restaurant.model.Ordination;
import java.util.List;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

/**
 *
 * @author naigo
 */
@Repository
public class OrdinationDao extends AbstractDao<Ordination> {

    public List<Ordination> findByDiningTable(String uuid) {
        TypedQuery<Ordination> q = getEntityManager().createQuery("FROM Ordination o where o.table.uuid = :uuid", Ordination.class);
        q.setParameter("uuid", uuid);
        List<Ordination> o = q.getResultList();
        return o;
    }

    public int nextProgressive(Evening e) {
        TypedQuery<Integer> q = getEntityManager().createQuery(
                "SELECT max(o.progressive) FROM Ordination o WHERE o.table.evening = :evening", Integer.class);
        q.setParameter("evening", e);
        Integer progressive = q.getSingleResult();
        if (progressive != null) {
            return progressive + 1;
        } else {
            return 1;
        }
    }

    @Override
    public Class<Ordination> getEntityClass() {
        return Ordination.class;
    }

}
