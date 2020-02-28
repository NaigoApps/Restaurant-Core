/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Evening;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

/**
 *
 * @author naigo
 */
@Repository
public class EveningDao extends AbstractDao<Evening> {

    public Evening findByDate(LocalDate date) {
        TypedQuery<Evening> q = getEntityManager().createQuery("FROM Evening e where e.day = :day", Evening.class);
        q.setParameter("day", date);
        Evening evening = null;
        List<Evening> evenings = q.getResultList();
        if (q.getResultList().size() > 0) {
            evening = evenings.get(0);
        }
        return evening;
    }

    @Override
    public Class<Evening> getEntityClass() {
        return Evening.class;
    }

}
