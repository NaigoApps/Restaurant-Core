/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Evening;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author naigo
 */
public class EveningDao extends Dao{
    
    public Evening findByDate(LocalDate date){
        TypedQuery<Evening> q = getEntityManager().createQuery("FROM Evening e where e.day = :day", Evening.class);
        q.setParameter("day", date);
        Evening evening = null;
        List<Evening> evenings = q.getResultList();
        if(q.getResultList().size() > 0){
            evening = evenings.get(0);
        }
        return evening;
    }
    
    public Evening findByUuid(String uuid){
        Query q = getEntityManager().createQuery("FROM Evening e where e.uuid = :uuid", Evening.class);
        q.setParameter("uuid", uuid);
        Evening evening = (Evening) q.getSingleResult();
        return evening;
    }
}
