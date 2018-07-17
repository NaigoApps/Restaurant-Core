/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.naigoapps.restaurant.model.dao;

import com.naigoapps.restaurant.model.Settings;
import com.naigoapps.restaurant.model.builders.SettingsBuilder;
import java.util.List;
import javax.persistence.Query;

/**
 *
 * @author naigo
 */
public class SettingsDao extends AbstractDao<Settings> {

    public Settings find() {
        Query q = getEntityManager().createQuery("FROM Settings s", Settings.class);
        List<Settings> settings = q.getResultList();
        if (settings.isEmpty()) {
            Settings s = new SettingsBuilder().getContent();
            getEntityManager().persist(s);
            return s;
        }
        return settings.get(0);
    }

    @Override
    public Class<Settings> getEntityClass() {
        return Settings.class;
    }

}
